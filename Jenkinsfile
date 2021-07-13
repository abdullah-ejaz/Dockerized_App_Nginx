@Library("my-shared-library") _
def app = 'Unknown'
pipeline{
    // agent { label 'slave || slave-jnlpx' }
    agent { label 'slave1' }
    environment {
        IMAGE_TAG="${env.BUILD_ID}"
        JOB_NAME = "Notify_Slack:${env.BUILD_ID}"
        BRANCH_NAME = "Main"
        REPOSITORY_URI = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO_NAME}"  
    }
    parameters {
        string(name: 'ENVIRONMENT', defaultValue: 'DEV', description: 'Where should I deploy?')
        string(name: 'service_name', defaultValue: 'appinservice', description: 'Pick Service to Deploy')
        text(name: 'AWS_DEFAULT_REGION', defaultValue: 'us-east-2', description: 'Enter region')
        text(name: 'AWS_ACCOUNT_ID', defaultValue: '489994096722', description: 'Enter id')
        text(name: 'IMAGE_REPO_NAME', defaultValue: 'abdullah_jenkins_ecr', description: 'Enter repo')
    }         
    stages{  
        stage('Logging into AWS ECR') {
            steps {
                script {
                    sh "aws ecr get-login-password --region ${params.AWS_DEFAULT_REGION} | docker login --username AWS --password-stdin ${params.AWS_ACCOUNT_ID}.dkr.ecr.${params.AWS_DEFAULT_REGION}.amazonaws.com"
                }
            }
        }         
        stage("Build image"){
            steps{
                script {
                    app = docker.build"${params.IMAGE_REPO_NAME}:${IMAGE_TAG}"
                }
            }   
        }
        stage("Test image"){
            when {
                expression { 
                    return params.ENVIRONMENT == 'DEV'
                }
            }            
            steps{
                script {
                    app.inside {            
                        sh 'echo "Tests passed"'        
                    } 
                }
            }   
        }   
        stage("Push image"){
            steps{
                script {
                    sh "docker tag ${params.IMAGE_REPO_NAME}:${IMAGE_TAG} ${REPOSITORY_URI}:$IMAGE_TAG"
                    sh "docker push ${params.AWS_ACCOUNT_ID}.dkr.ecr.${params.AWS_DEFAULT_REGION}.amazonaws.com/${params.IMAGE_REPO_NAME}:${IMAGE_TAG}"
                }
            }
        }
        stage("register Task defintion"){
            steps{
                script {
                    sh "sed -i 's/%BUILD_ID%/${BUILD_ID}/g' task.json"
                    sh "aws ecs register-task-definition --cli-input-json file://task.json"
                }
            }
        }     
        stage("Update service"){
            steps{
                script {
                    sh "aws ecs update-service --cluster abdullah-jenkins-fargate --service ${params.service_name} --task-definition task --desired-count 1"
                }
            }
        }                     
    }
        post {
            success {
                script {
                    if ( env.BRANCH_NAME == 'Main')
                    {
                        slacksuccess.success()
                    }    
                    else {
                        slacksuccess.failure()
                    }
            }           
        } 
    }    
}         
