def call (){
	pipeline {
		agent any
		
		environment {
			STAGE = ''
			GRADLEPIPELINE = "BuildJar;Sonar;run;Nexus"
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
									gradle(params.STAGES)
						} else {
								if(isStageValido(MAVENPIPELINE))
									maven(params.STAGES)
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
	
	String instancia = ''
	def estado = false
	println "Ejecutando funcion: isStageValido"
	if (params.STAGES.isEmpty()){
		instancia = pipeline
		println "VACIO"
	}
	else
		instancia = params.STAGES


			
	instancia.tokenize(";").each { paramStage ->
	println paramStage
				    
		pipeline.tokenize(";").each { stage ->
			if(stage.equals(paramStage)){
				estado = true
				println "Existe Stage: ${stage}"
			}	
		}
		if(!estado) return
		estado = false
	}

	return estado;
	}


return this;