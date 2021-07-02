def app = 'Unknown'
pipeline{
    agent any
    environment {
    registry = '489994096722.dkr.ecr.us-east-2.amazonaws.com/abdullah_jenkins_ecr'
    // registryCredential = 'ecr:us-east-2'
    AWS_DEFAULT_REGION = 'us-east-2'
    AWS_ACCOUNT_ID="489994096722"
    IMAGE_REPO_NAME="abdullah_jenkins_ecr"
    IMAGE_TAG="abdullah-sample-app"
    REPOSITORY_URI = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO_NAME}"   
    Public_Subnet_2 = "subnet-09c93874"
    Public_Subnet_1 = "subnet-eaa81381"
    service_name = "appinservice"
             }    
    stages{   
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
                }
            }
        }
        stage("register Task defintion"){
            steps{
                script {
                    sh "aws ecs register-task-definition --cli-input-json file://task.json"
                }
            }
        }     
        stage("Create service"){
            steps{
                script {
                    aws ecs update-service --service my-http-service --task-definition amazon-ecs-sample
                    sh "aws ecs create-service --cluster abdullah-jenkins-fargate --service-name ${service_name} --task-definition abdullah-jenkins-ecs-app --desired-count 1  --launch-type 'FARGATE' --platform-version 'LATEST' --network-configuration 'awsvpcConfiguration={subnets=[${Public_Subnet_1},${Public_Subnet_2}],securityGroups=[sg-0fe33bb1bc74e8fa9],assignPublicIp=ENABLED}' "
                }
            }
        }     
        stage("Deploy service"){
            steps{
                script {
                    sh "deploy --service app --task-definition abdullah-jenkins-ecs-app"       
                }
            }
        }               
    }
}  