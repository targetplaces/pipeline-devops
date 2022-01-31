def call (){
	pipeline {
		agent any
		
		environment {
			STAGE = ''
			GRADLEPIPELINE = "BuildTestJar;Sonar;run;Nexus"
			MAVENPIPELINE = "Build;Sonar;run;Nexus"
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
								if(isStageValido(GRADLEPIPELINE))
									gradle(params.STAGE)
						} else {
								if(isStageValido(MAVENPIPELINE))
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
	
	String[] instancia = []

	println "Ejecutando funcion: isStageValido"
	if (params.STAGES.isEmpty())
		instancia = pipeline.split(';')
	else
		instancia = params.STAGES.split(';')

    def estado = true

		for (int i = 0; i < pipeline.length; i++) {
			println instancia[i]
						instancia.tokenize(";").each { stage ->
							if(!stage.equals(pipeline[i])){
								println "NO EXISTE STAGE: ${stage}"
								estado = false
							}
			
						}
		}

	return estado;
	}


return this;