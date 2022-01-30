def call(String STAGE){
  
  figlet STAGE
  
	stage('BuildTestJar'){
            when {
                expression { myStage == 'Stage1' }
            }
					println "Stage: ${env.STAGE_NAME}"
					STAGE = env.STAGE_NAME
					sh "chmod +x gradlew"
					sh "./gradlew clean build"

		}
		stage('Sonar'){
					println "Stage: ${env.STAGE_NAME}"
					STAGE = env.STAGE_NAME
					    def scannerHome = tool 'sonar-scanner';
					    withSonarQubeEnv('sonnarqube-server'){
						sh "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=ejemplo-gradle -Dsonar.sources=src -Dsonar.java.binaries=build"
					    }
		}
		stage('run'){

					println "Stage: ${env.STAGE_NAME}"
					STAGE = env.STAGE_NAME
					sh "nohup bash gradlew bootRun &"
					sleep 20

		}
		stage('Test'){

					println "Stage: ${env.STAGE_NAME}"
					STAGE = env.STAGE_NAME
					sh "curl -X GET 'http://localhost:8081/rest/mscovid/test?msg=testing'"

		}
		stage('Nexus'){

					println "Stage: ${env.STAGE_NAME}"
					STAGE = env.STAGE_NAME
					nexusPublisher nexusInstanceId: 'nexus',
					nexusRepositoryId: 'test-nexus',
					packages: [
					    [
						$class: 'MavenPackage',
						mavenAssetList: [
						    [classifier: '', extension: '', filePath: 'build/libs/DevOpsUsach2020-0.0.1.jar']
						],
						mavenCoordinate: [
						    artifactId: 'DevOpsUsach2020',
						    groupId: 'com.devopsusach2020',
						    packaging: 'jar',
						    version: '0.0.1'
						]
					    ]
					]
				    

		}
}

return this;
