# Config Server

## Overview
The config server is responsible for providing all the downstream services with 
the configuration that they need to connect to databases. The config repo is not hosted
on any public repo, but is contained in a folder contained in the root directory named
`config-repo`. You have to configure the path to this folder in your environmental variable if
you are running the application in `development` mode.

## Port 
The config server runs on port `8888` by default.