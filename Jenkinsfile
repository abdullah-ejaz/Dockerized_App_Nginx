def app = 'Unknown'
pipeline{
    agent any
    environment {
    registry = '489994096722.dkr.ecr.us-east-2.amazonaws.com/abdullah_jenkins_ecr'
    // registryCredential = 'ecr:us-east-2'
    AWS_DEFAULT_REGION = 'us-east-2'
    AWS_ACCOUNT_ID="489994096722"
    IMAGE_REPO_NAME="abdullah_jenkins_ecr"
    IMAGE_TAG="abdullah-static-app"
    REPOSITORY_URI = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO_NAME}"    
             }       
        stage("Build image"){
            steps{
                script {
                    app = docker.build"${IMAGE_REPO_NAME}:${IMAGE_TAG}"
                }
            }   
        }
        stage("Test image"){
            steps{
                script {
                    app.inside {            
                        sh 'echo "Tests passed"'        
                    } 
                }
            }   
        }
            stage('Logging into AWS ECR') {
            steps {
                script {
                sh "aws ecr get-login-password --region ${AWS_DEFAULT_REGION} | docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com"
                }
            }
        }    
        stage("Push image"){
            steps{
                script {
                    sh "docker tag ${IMAGE_REPO_NAME}:${IMAGE_TAG} ${REPOSITORY_URI}:$IMAGE_TAG"
                    sh "docker push ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO_NAME}:${IMAGE_TAG}"
                    // configure registry
                    // docker.withRegistry( '489994096722.dkr.ecr.us-east-2.amazonaws.com' , 'ecr:us-east-2:abdullah_jenkins_ecr')
                    // {
                    //     def myImage = app.push("abd-sample-pythonapp")            
                    //     myImage.push("latest")     
                    // }
                }
            }
        }   
        stage("Push image"){
            steps{
                script {
    sh "aws ecs create-service --cluster abdullah-jenkins-fargate --service-name --task-definition  --desired-count 1  --launch-type 'FARGATE' --platform-version 'LATEST' --network-configuration 'awsvpcConfiguration={subnets=[],securityGroups=[],assignPublicIp=ENABLED}' "
                }
            }
        }              
    }
}  