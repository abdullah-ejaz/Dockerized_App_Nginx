def success () {
    slackSend color: "good", channel: 'U01SLG5TVQF', message: "Build Succeeded. The Job: ${env.JOB_NAME} has been built successfully with Build# ${env.BUILD_NUMBER} which triggered with change in ${env.BRANCH_NAME} Branch with Job URL: ${env.BUILD_URL}"
}
def failure () {
    slackSend color: "danger", channel: 'U01SLG5TVQF', message: "Build Failed. The Job: ${env.JOB_NAME} has been failed with Build# ${env.BUILD_NUMBER} which triggered with change in ${env.BRANCH_NAME} Branch with Job URL: ${env.BUILD_URL}" 
}
