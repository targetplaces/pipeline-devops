def call (){
	pipeline {
		agent any
		
		environment {
			STAGE = ''
			GRADLEPIPELINE = "BuildTestJar,Sonar,run,Nexus"
			MAVENPIPELINE = "Build,Sonar,run,Nexus"
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

						if (params.buildTool=='gradle') {
								isStageValido(GRADLEPIPELINE)
								gradle(params.STAGE)
						} else {
								isStageValido(MAVENPIPELINE)
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

def isStageValido(pipeline){
	
	def instancia = params.STAGE.split(';')

		for (int i = 0; i < instancia.length; i++) {
			
						pipeline.tokenize(",").each { stage ->
							if(!stage.equals(pipeline[i])){
								return false;
							}
			
						}
		}
	}


return this;