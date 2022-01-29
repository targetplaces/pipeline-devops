def call (){
	pipeline {
		agent any
		
		environment {
			STAGE = ''
			gradlePipeline = ['BuildTestJar', 'Sonar', 'run', 'Nexus']
			mavenPipeline = ['Build', 'Sonar', 'run', 'Nexus']
		}
		
		parameters{ string(name: 'STAGE', defaultValue: '' )}
		
		parameters {
		choice choices: ['gradle', 'maven'], description: 'indicar herramienta de construcción', name: 'buildTool'
		}

		stages {
			stage('pipeline'){
				steps{
					script{
						println 'pipeline'

						def instancia = params.STAGE.split(';')

						for (int i = 0; i < instancia.length; i++) {
							for (int j = 0; j < pipeline.length; j++) {
								if(!instancia[i].equals(pipeline[i])){
									println 'No existe el STAGE ${isntancia[i]}'
									return false;
								}
							}
						}
						if (params.buildTool=='gradle') {
								gradle(params.STAGE)
						} else {
								maven(params.STAGE)
						}

					}
				}
			}
			
		}
		post {
			success{
				slackSend(color: 'good', channel: "U02MBA9FXHD", message: "[${env.BUILD_USER}][${env.JOB_NAME}][${params.STAGE}] Ejecución Exitosa.")
			}
			failure {
				slackSend(color: 'danger', channel: "U02MBA9FXHD", message: "[${env.BUILD_USER}][${env.JOB_NAME}][${params.STAGE}] Ejecución fallida en Stage: ${STAGE}")
			}
		}
	}
}

return this;

