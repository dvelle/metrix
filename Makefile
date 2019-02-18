.PHONY: am

os:=$(shell uname | tr [:upper:] [:lower:])
amV=0.15.2

prom:
	prometheus

am:
	alertmanager/alertmanager

listener:
	FLASK_ENV=development FLASK_APP=listener.py flask run --port=5001

setup-am:
	@mkdir -p alertmanager
	curl --silent -L https://github.com/prometheus/alertmanager/releases/download/v${amV}/alertmanager-${amV}.${os}-amd64.tar.gz | tar -xz --strip 1 -C alertmanager

