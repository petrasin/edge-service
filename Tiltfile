custom_build(
    ref = 'edge-service',
    command = './gradlew bootBuildImage --imageName $EXPECTED_REF',
    deps = ['build.gradle', 'src']
)

k8s_yaml(['k8s/deployment.yml', 'k8s/service.yml'])

k8s_resource('edge-service', port_forwards=['9000'])