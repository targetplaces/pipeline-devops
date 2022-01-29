def call (){
	pipeline {
		agent any
		
		environment {
			STAGE = ''
			gradlePipeline = ['BuildTestJar', 'Sonar', 'run', 'Nexus']
			mavenPipeline = ['Build', 'Sonar', 'run', 'Nexus']
		}
		
<<<<<<< HEAD
		parameters{ string(name: 'STAGE', defaultValue: '' )}
		
		parameters {
		choice choices: ['gradle', 'maven'], description: 'indicar herramienta de construcción', name: 'buildTool'
		}
=======
		parameters{ string(name: 'STAGE2', defaultValue: '' )}
>>>>>>> 2a552f78a3797b78d8f35304a160dda55f007bb3

		stages {
			stage('pipeline'){
				steps{
					script{
						println 'pipeline'
<<<<<<< HEAD

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
=======
						println params.STAGE
						
							println 'opcion1'
								gradle(params.STAGE)
>>>>>>> 2a552f78a3797b78d8f35304a160dda55f007bb3

					}
				}
			}
			
		}
		post {
			success{
<<<<<<< HEAD
				slackSend(color: 'good', channel: "U02MBA9FXHD", message: "[${env.BUILD_USER}][${env.JOB_NAME}][${params.STAGE}] Ejecución Exitosa.")
			}
			failure {
				slackSend(color: 'danger', channel: "U02MBA9FXHD", message: "[${env.BUILD_USER}][${env.JOB_NAME}][${params.STAGE}] Ejecución fallida en Stage: ${STAGE}")
=======
				slackSend(color: 'good', channel: "U02MBA9FXHD", message: "[${env.BUILD_USER}][${env.JOB_NAME}][${params.STAGE2}] Ejecución Exitosa.")
			}
			failure {
				slackSend(color: 'danger', channel: "U02MBA9FXHD", message: "[${env.BUILD_USER}][${env.JOB_NAME}][${params.STAGE2}] Ejecución fallida en Stage: ${STAGE}")
>>>>>>> 2a552f78a3797b78d8f35304a160dda55f007bb3
			}
		}
	}
}
<<<<<<< HEAD

return this;

=======
return this;
>>>>>>> 2a552f78a3797b78d8f35304a160dda55f007bb3
