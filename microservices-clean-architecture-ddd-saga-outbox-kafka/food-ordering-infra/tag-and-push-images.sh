gcloud auth login

docker tag com.training.food.order/customer.service:$1 europe-west6-docker.pkg.dev/food-ordering-system-370111/food-ordering-system-repository/customer.service:$1
docker tag com.training.food.order/payment.service:$1 europe-west6-docker.pkg.dev/food-ordering-system-370111/food-ordering-system-repository/payment.service:$1
docker tag com.training.food.order/restaurant.service:$1 europe-west6-docker.pkg.dev/food-ordering-system-370111/food-ordering-system-repository/restaurant.service:$1
docker tag com.training.food.order/order.service:$1 europe-west6-docker.pkg.dev/food-ordering-system-370111/food-ordering-system-repository/order.service:$1

docker push europe-west6-docker.pkg.dev/food-ordering-system-370111/food-ordering-system-repository/order.service:$1
docker push europe-west6-docker.pkg.dev/food-ordering-system-370111/food-ordering-system-repository/payment.service:$1
docker push europe-west6-docker.pkg.dev/food-ordering-system-370111/food-ordering-system-repository/restaurant.service:$1
docker push europe-west6-docker.pkg.dev/food-ordering-system-370111/food-ordering-system-repository/customer.service:$1