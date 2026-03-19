# Kubernetes manifests (k3d + local registry)

Este pacote sobe os servicos `discovery`, `log`, `gateway` e um `postgres` no namespace `microservices`.

## Estrutura

- `k8s/base`: manifests base para todos os ambientes
- `k8s/overlays/local-k3d`: overlay para uso local com k3d e registry local

## 1) Criar cluster k3d com registry local

Se voce ainda nao tem um cluster configurado com registry, este fluxo cria tudo do zero:

```bash
k3d registry create local-reg --port 5000
k3d cluster create microservices --servers 1 --agents 1 --registry-use k3d-local-reg:5000 -p "8080:80@loadbalancer"
```

Se voce ja possui cluster, confirme se ele enxerga o registry que voce usa para `localhost:5000`.

## 2) Build e push das imagens

No root do monorepo:

```bash
./mvnw -pl discovery -am spring-boot:build-image -Dspring-boot.build-image.imageName=localhost:5000/micro/discovery:1.0.0
./mvnw -pl log -am spring-boot:build-image -Dspring-boot.build-image.imageName=localhost:5000/micro/log:1.0.0
./mvnw -pl gateway -am spring-boot:build-image -Dspring-boot.build-image.imageName=localhost:5000/micro/gateway:1.0.0

docker push localhost:5000/micro/discovery:1.0.0
docker push localhost:5000/micro/log:1.0.0
docker push localhost:5000/micro/gateway:1.0.0
```

Se quiser mudar a tag, atualize `k8s/overlays/local-k3d/kustomization.yaml`.

## 3) Aplicar os manifests

```bash
kubectl apply -k k8s/overlays/local-k3d
```

## 4) Validar rollout

```bash
kubectl get all -n microservices
kubectl get ingress -n microservices
kubectl logs -n microservices deploy/discovery
kubectl logs -n microservices deploy/log
kubectl logs -n microservices deploy/gateway
```

## 5) Acesso

Com Traefik (default do k3s/k3d), o gateway fica em:

- `http://gateway.localtest.me/`
- Exemplo de rota: `http://gateway.localtest.me/gateway/log/course`

## Variaveis importantes

As principais configuracoes estao em:

- `k8s/base/configmap-app.yaml`
- `k8s/base/secret-app.yaml`

Ajuste esses arquivos para seu ambiente (principalmente credenciais e URL do banco).

