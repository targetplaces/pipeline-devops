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
	
	String[] instancia = []
	String [] ejecutarStage = pipeline.split(";");

	println "Ejecutando funcion: isStageValido"
	if (params.STAGES.isEmpty()){
		instancia = pipeline.split(";");
	}
	else
		instancia = params.STAGES.split(";");

    if (ejecutarStage.findAll { e -> instancia.contains( e ) }.size() == 0) {
        throw new Exception('Al menos una stage es inválida. Stages válidas: ' + ejecutarStage.join(', ') + '. Recibe: ' + instancia.join(', '))
		return false
    }
	/*		
	instancia.tokenize(";").each { paramStage ->
	println paramStage
		estado = false		    
		pipeline.tokenize(";").each { stage ->
			if(stage.equals(paramStage)){
				estado = true
				println "Existe Stage: ${stage}"
			}	
		}
		if(!estado) return
	}

	return estado;
	*/
	}


return this;