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
	
	println "Ejecutando funcion: isStageValido"
	if (params.STAGES.isEmpty())
		def instancia = pipeline.split(';')
	else
		def instancia = params.STAGES.split(';')

    def estado = true
	println instancia
	println "Antes del for"
		for (int i = 0; i < instancia.length; i++) {
			println "luego del for"
			println instancia[i]
						pipeline.tokenize(";").each { stage ->
							if(!stage.equals(instancia[i])){
								println stage
								estado = false
							}
			
						}
		}

	return estado;
	}


return this;