/// <reference path="../../../typings/angularjs/angular-resource.d.ts" />
/// <reference path="Application.ts" />

hf.marketdataprovider.factories.factory('hf.marketdataprovider.apiResource', ['$resource',
        ($resource:ng.resource.IResourceService):
            ng.resource.IResourceClass<ng.resource.IResource<any>> => {
            var apiRoot:ng.resource.IResourceClass<ng.resource.IResource<any>> =
                $resource('http://localhost:8080/getProducts');

            return apiRoot;
        }]);