#!/bin/bash
# cleanup.sh

echo "Stopping port-forward processes..."
pkill -f "kubectl port-forward"

echo "Deleting Kubernetes resources..."
kubectl delete deployment movies-api-deployment --ignore-not-found=true
kubectl delete deployment mysql-statefulset --ignore-not-found=true
kubectl delete service movies-api-service --ignore-not-found=true
kubectl delete service mysql-service --ignore-not-found=true
kubectl delete cronjob.batch/movies-api-test-cronjob
echo "Verifying cleanup..."
kubectl get all

echo "Cleanup complete!"