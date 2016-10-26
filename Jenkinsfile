#!/usr/bin/groovy
node{
  ws{
    checkout scm
    sh "git remote set-url origin git@github.com:fabric8io/ipaas-console.git"

    def pipeline = load 'release.groovy'

    stage 'Stage'
    def stagedProject = pipeline.stage()

    //stage 'Deploy'
    //pipeline.deploy(OPENSHIFT_URL, OPENSHIFT_DOMAIN, OPENSHIFT_STAGING_DOCKER_REGISTRY_URL)

    //stage 'Approve'
    //pipeline.approveRelease(stagedProject)

    stage 'Promote'
    pipeline.release(stagedProject)

    stage 'Update downstream dependencies'
    pipeline.updateDownstreamDependencies(stagedProject)
  }
}
