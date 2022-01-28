def call (){
	pipeline {
		agent any
		
		environment {
			STAGE = ''
		}
		
		options([parameters([text('stage')])])

		stages {
			stage('pipeline'){
				steps{
					script{
						println 'pipeline'
						println params.buildTool
						

						if (params.stage=='gradle') {
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
				slackSend(color: 'good', channel: "U02MBA9FXHD", message: "[${env.BUILD_USER}][${env.JOB_NAME}][${params.buildTool}] Ejecución Exitosa.")
			}
			failure {
				slackSend(color: 'danger', channel: "U02MBA9FXHD", message: "[${env.BUILD_USER}][${env.JOB_NAME}][${params.buildTool}] Ejecución fallida en Stage: ${STAGE}")
			}
		}
	}
}
return this;
