/// <reference path="../../../typings/angularjs/angular.d.ts" />

module hf.marketdataprovider {
    export var app = angular.module('marketdataprovider',
        ['hf.marketdataprovider.controllers', 'hf.marketdataprovider.factories','hf.marketdataprovider.services']);

    export var controllers = angular.module('hf.marketdataprovider.controllers', []);
    export var factories = angular.module('hf.marketdataprovider.factories', ['ngResource']);
    export var services = angular.module('hf.marketdataprovider.services', ['hf.marketdataprovider.factories']);
}