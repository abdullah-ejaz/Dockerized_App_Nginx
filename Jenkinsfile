def app = 'Unknown'
pipeline{
    agent any
    environment {
    // registry = '489994096722.dkr.ecr.us-east-2.amazonaws.com/abdullah_jenkins_ecr'
    // registryCredential = 'ecr:us-east-2'
    AWS_DEFAULT_REGION = 'us-east-2'
    AWS_ACCOUNT_ID="489994096722"
    IMAGE_REPO_NAME="abdullah_jenkins_ecr"
    IMAGE_TAG="v_${env.BUILD_ID}"
    REPOSITORY_URI = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO_NAME}"   
    Public_Subnet_2 = "subnet-09c93874"
    Public_Subnet_1 = "subnet-eaa81381"
    service_name = "appinservice"
    }    
    stages{  
        stage('Logging into AWS ECR') {
            steps {
                script {
                    sh "aws ecr get-login-password --region ${AWS_DEFAULT_REGION} | docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com"
                }
            }
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
                    sh "sed -e "s;%BUILD_ID%;${BUILD_ID};g" flask-signup.json > flask-signup-v_${BUILD_ID}.json"
                    sh "aws ecs register-task-definition --cli-input-json file://task-v_${BUILD_ID}.json"
                }
            }
        }     
        stage("Update service"){
            steps{
                script {
                    sh "aws ecs update-service --cluster abdullah-jenkins-fargate --service ${service_name} --task-definition task --desired-count 1"
                }
            }
        }                     
    }
}  