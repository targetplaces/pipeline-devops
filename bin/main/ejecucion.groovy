def call (){
	pipeline {
		agent any
		
		environment {
			STAGE = ""
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

						if (params.buildTool=='gradle') {

								if(isStageValido(GRADLEPIPELINE)){
									println "STAGES:: ${env.STAGE}"
									gradle(env.STAGE)
								}
									
						} else {
								if(isStageValido(MAVENPIPELINE))
									maven(env.STAGE)
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
	String [] instancia = []

	if (params.STAGES.isEmpty()){
		instancia = ejecutarStage;
		env.STAGE = pipeline;
	}
	else
		instancia = params.STAGES.split(";");

	println "pipeline: ${pipeline}"
	println "ENV STAGE: ${env.STAGE}"

    if (ejecutarStage.findAll { e -> instancia.contains( e ) }.size() == 0) {
        println 'ERROR EN STAGES INGRESADAS: STAGES VALIDAS: ' + ejecutarStage.join(', ') + '. STAGE INGRESADAS: ' + instancia.join(', ')
		return false;
    }

	return true
	}


return this;