#!/usr/bin/groovy
def externalImages(){
  return ['ipaas-console']
}

def repo(){
 return 'fabric8io/ipaas-console'
}

def stage(){
  return stageProject{
    project = repo()
    useGitTagForNextVersion = true
    extraImagesToStage = externalImages()
  }
}

def deploy(openshiftUrl, openshiftDomain, kubernetesUrl, openshiftStagingDockerRegistryUrl){

  deployRemoteOpenShift{
    url = openshiftUrl
    domain = openshiftDomain
    stagingDockerRegistry = openshiftStagingDockerRegistryUrl
  }

}

def updateDownstreamDependencies(stagedProject) {
  pushPomPropertyChangePR {
    propertyName = 'ipaas.console.version'
    projects = [
            'fabric8io/ipaas-platform'
    ]
    version = stagedProject[1]
  }
}

def approveRelease(project){
  def releaseVersion = project[1]
  approve{
    room = null
    version = releaseVersion
    console = null
    environment = 'fabric8'
  }
}

def release(project){
  releaseProject{
    stagedProject = project
    useGitTagForNextVersion = true
    helmPush = false
    groupId = 'io.fabric8.ipaas'
    githubOrganisation = 'fabric8io'
    artifactIdToWatchInCentral = 'ipaas-console'
    artifactExtensionToWatchInCentral = 'pom'
    promoteToDockerRegistry = 'docker.io'
    dockerOrganisation = 'fabric8'
    extraImagesToTag = externalImages()
  }
}

def mergePullRequest(prId){
  mergeAndWaitForPullRequest{
    project = repo()
    pullRequestId = prId
  }

}
return this;
