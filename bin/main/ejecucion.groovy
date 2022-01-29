def call {
	pipeline {
		agent any
		
		environment {
			STAGE = ''
		}
		
		parameters {
		  choice choices: ['gradle', 'maven'], description: 'indicar herramienta de construcción', name: 'buildTool'
		}

		stages {
			stage('pipeline'){
				steps{
					script{
						println 'pipeline'
						

						if (params.buildTool=='gradle') {
								gradle(verifyBranchName())
						} else {
								def ejecucion = load 'maven.groovy'
								maven(verifyBranchName())
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

def verifyBranchName()
{
	//def isCiorCd = (env.GIT_BRANCH.constains('feature-')) ? 'CI' : 'CD'

	if (env.GIT_BRANCH.contains('feature-') || env.GIT_BRANCH.contains('develop')){
		return 'CI'
	} else{
		return 'CD'
	}
}
return this;