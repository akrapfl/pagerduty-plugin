// // Builds the plugin using https://github.com/jenkins-infra/pipeline-library
// buildPlugin(configurations: [
//   // Test the long-term support end of the compatibility spectrum (i.e., the minimum required
//   // Jenkins version).
//   //[ platform: 'linux', jdk: '8', jenkins: null ],

//   // Test the common case (i.e., a recent LTS release) on both Linux and Windows.
//   [ platform: 'linux', jdk: '8', jenkins: '2.263.1' ]
//   //[ platform: 'windows', jdk: '8', jenkins: '2.263.1' ],

//   // Test the bleeding edge of the compatibility spectrum (i.e., the latest supported Java runtime).
//   //[ platform: 'linux', jdk: '11', jenkins: '2.263.1' ],
// ])

pipeline {
    agent {
        docker {
            image 'maven:3.6-jdk-8'
            label 'docker'
        }
    }

   stages {
      stage('build') {
         steps {
             sh 'mvn clean install'
         }
      }
   }
}
