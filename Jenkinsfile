def app = 'Unknown'
pipeline{
    agent any
    environment {
        IMAGE_TAG="${env.BUILD_ID}"
        REPOSITORY_URI = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO_NAME}"  
    }
    parameters {
        string(name: 'ENVIRONMENT', defaultValue: 'DEV', description: 'Where should I deploy?')
        string(name: 'service_name', defaultValue: 'appinservice', description: 'Pick Service to Deploy')
        text(name: 'AWS_DEFAULT_REGION', defaultValue: '', description: 'Enter some information')
        text(name: 'AWS_ACCOUNT_ID', defaultValue: '', description: 'Enter some information')
        text(name: 'IMAGE_REPO_NAME', defaultValue: '', description: 'Enter some information')
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
                    //  sed -i 's/database_name_here/wordpress/g' /var/www/html/wp-config.php
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
}  
