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
<<<<<<< HEAD
						

						if (params.buildTool=='gradle') {
								gradle(verifyBranchName())
						} else {
								def ejecucion = load 'maven.groovy'
								maven(verifyBranchName())
						}
=======
						println params.STAGE
						
							println 'opcion1'
								gradle(params.STAGE)
>>>>>>> 2a552f78a3797b78d8f35304a160dda55f007bb3


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
<<<<<<< HEAD

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
=======
return this;
>>>>>>> 2a552f78a3797b78d8f35304a160dda55f007bb3
