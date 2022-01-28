def call (){
	pipeline {
		agent any
		
		environment {
			STAGE = ''
		}
		
		parameters{ string(name: 'STAGE2', defaultValue: '' )}

		stages {
			stage('pipeline'){
				steps{
					script{
						println 'pipeline'
						println params.buildTool
						

						if (params.STAGE2=='gradle') {
								gradle()
						} else {
								def ejecucion = load 'maven.groovy'
								maven()
						}


					}
				}
			}
			
		}
		post {
			success{
				slackSend(color: 'good', channel: "U02MBA9FXHD", message: "[${env.BUILD_USER}][${env.JOB_NAME}][${params.STAGE2}] Ejecución Exitosa.")
			}
			failure {
				slackSend(color: 'danger', channel: "U02MBA9FXHD", message: "[${env.BUILD_USER}][${env.JOB_NAME}][${params.STAGE2}] Ejecución fallida en Stage: ${STAGE}")
			}
		}
	}
}
return this;
