def call (){
	pipeline {
		agent any
		
		environment {
			STAGE = ''
			GRADLEPIPELINE = "BuildJar;Sonar;run;Nexus"
			MAVENPIPELINE = "Build;Sonar;run;Nexus"
			instancia = ""
		}

		parameters {
			string(name: 'STAGES', defaultValue: '' )
			choice choices: ['gradle', 'maven'], description: 'indicar herramienta de construcción', name: 'buildTool'
		}

		stages {
			stage('pipeline'){
				steps{
					script{

						if (params.buildTool=='gradle') {

								if(isStageValido(GRADLEPIPELINE))
									gradle(env.instancia)
						} else {
								if(isStageValido(MAVENPIPELINE))
									maven(env.instancia)
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
	
	
	String [] ejecutarStage = pipeline.split(";");

	if (params.STAGES.isEmpty())
		env.instancia = ejecutarStage;
	else
		env.instancia = params.STAGES.split(";");

	println "STAGES:"
	println env.instancia
	
    if (ejecutarStage.findAll { e -> env.instancia.contains( e ) }.size() == 0) {
        println 'ERROR EN STAGES INGRESADAS: STAGES VALIDAS: ' + ejecutarStage.join(', ') + '. STAGE INGRESADAS: ' + env.instancia.join(', ')
		return false;
    }

	return true
	}


return this;