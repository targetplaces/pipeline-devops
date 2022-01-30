def call (){
	pipeline {
		agent any
		
		environment {
			STAGE = ''
			GRADLEPIPELINE = "BuildTestJar,Sonar,run,Nexus"
			MAVENPIPELINE = "Build,Sonar,run,Nexus"
		}

		parameters {
			string(name: 'STAGES', defaultValue: '' )
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
				slackSend(color: 'good', channel: "U02MBA9FXHD", message: "[${env.BUILD_USER}][${env.JOB_NAME}][${params.STAGES}] Ejecución Exitosa.")
			}
			failure {
				slackSend(color: 'danger', channel: "U02MBA9FXHD", message: "[${env.BUILD_USER}][${env.JOB_NAME}][${params.STAGES}] Ejecución fallida en Stage: ${env.STAGE}")
			}
		}
	}
}

def isStageValido(pipeline){
	
	println "Ejecutando funcion: isStageValido"
	def instancia = params.STAGES.split(';')

		for (int i = 0; i < instancia.length; i++) {
			
						pipeline.tokenize(",").each { stage ->
							if(!stage.equals(instancia[i])){
								return false;
							}
			
						}
		}
	}


return this;