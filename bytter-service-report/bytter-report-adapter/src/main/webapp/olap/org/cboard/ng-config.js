/**
 * Created by Peter on 2016/10/22.
 */

// 对angularJS的$stateProvider进行自定义配置
angular.module('cBoard').config(['$stateProvider', function ($stateProvider) {
    // var resourceVersion = Date.now();
    //angularJs路由配置
    $stateProvider
        .state('olapRoute', {    // 页面导航
            url: '',
            // templateUrl: 'org/cboard/view/cboard/olapRoutePage.html',
            templateUrl: basePath + '/olap/org/cboard/view/cboard/olapRoutePage.html?_btv=' + resourceVersion,
            controller: 'olapRouteCtrl'
        })
        .state('dashboard', {   // 看板
            url: '/dashboard',
            abstract: true,
            template: '<div ui-view></div>'
        })
        .state('mine', {
            url: '/mine',
            abstract: true,
            template: '<div ui-view></div>'
        })
        .state('mine.view', {
            url: '/{id}',
            params: {id: null},
            templateUrl: basePath + '/olap/org/cboard/view/dashboard/view.html?_btv=' + resourceVersion,
            controller: 'dashboardViewCtrl'
        })
        .state('dashboard.category', {  // 看板分类
            url: '/{category}',
            params: {category: null},
            abstract: true,
            template: '<div ui-view></div>',
        })
        .state('dashboard.category.view', {
            url: '/{id}',
            templateUrl: basePath + '/olap/org/cboard/view/dashboard/view.html?_btv=' + resourceVersion,
            controller: 'dashboardViewCtrl'
        })
        .state('dashboard.dashboardList', { // 报表列表查看页面
            url: '/dashboardList',
            params: {id: null},
            templateUrl: basePath + '/olap/org/cboard/view/dashboard/dashboardList.html?_btv=' + resourceVersion,
            controller: 'dashboardListViewCtrl'
        })
        .state('config', {
            url: '/config',
            abstract: true,
            template: '<div ui-view></div>'
        })
        .state('config.board', {
            url: '/board/{boardId}',
            params: {boardId: null},
            templateUrl: basePath + '/olap/org/cboard/view/config/board.html?_btv=' + resourceVersion,
            controller: 'boardCtrl'
        })
        .state('config.widget', {
            url: '/widget?id&datasetId',
            params: {id: null, datasetId: null},
            templateUrl: basePath + '/olap/org/cboard/view/config/widget.html?_btv=' + resourceVersion,
            controller: 'widgetCtrl'
        })
        .state('config.category', {
            url: '/category',
            templateUrl: basePath + '/olap/org/cboard/view/config/category.html?_btv=' + resourceVersion,
            controller: 'categoryCtrl'
        })
        .state('config.dataset', {
            url: '/dataset/{id}',
            params: {id: null},
            templateUrl: basePath + '/olap/org/cboard/view/config/dataset.html?_btv=' + resourceVersion,
            controller: 'datasetCtrl'
        })
        .state('config.importDataset', {    // 导入数据集页面
            url: '/importDataset',
            templateUrl: basePath + '/olap/org/cboard/view/config/importDataSet.html?_btv=' + resourceVersion,
            controller: 'importDataSetCtrl'
        });

}]);

// 创建sessionHelper工具对象
angular.module('cBoard').factory('sessionHelper', ["$rootScope", "$q", function ($rootScope, $q) {
    var sessionHelper = {
        responseError: function (response) {
            if (response.data.status == 2) {
                if ($rootScope.alert) {
                    $rootScope.alert(response.data.msg);
                }
            }
            return $q.reject(response);
        }
    };
    return sessionHelper;
}]);

// 对angularJS的内置服务$httpProvider进行自定义配置
angular.module('cBoard').config(function ($httpProvider) {
    $httpProvider.defaults.headers.put['Content-Type'] = 'application/x-www-form-urlencoded';
    $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';

    // Override $http service's default transformRequest
    $httpProvider.defaults.transformRequest = [function (data) {
        /**
         * The workhorse; converts an object to x-www-form-urlencoded serialization.
         * @param {Object} obj
         * @return {String}
         */
        var param = function (obj) {
            var query = '';
            var name, value, fullSubName, subName, subValue, innerObj, i;

            for (name in obj) {
                value = obj[name];

                if (value instanceof Array) {
                    for (i = 0; i < value.length; ++i) {
                        subValue = value[i];
                        fullSubName = name + '[' + i + ']';
                        innerObj = {};
                        innerObj[fullSubName] = subValue;
                        query += param(innerObj) + '&';
                    }
                } else if (value instanceof Object) {
                    for (subName in value) {
                        subValue = value[subName];
                        fullSubName = name + '[' + subName + ']';
                        innerObj = {};
                        innerObj[fullSubName] = subValue;
                        query += param(innerObj) + '&';
                    }
                } else if (value !== undefined && value !== null) {
                    query += encodeURIComponent(name) + '='
                        + encodeURIComponent(value) + '&';
                }
            }

            return query.length ? query.substr(0, query.length - 1) : query;
        };

        return angular.isObject(data) && String(data) !== '[object File]'
            ? param(data)
            : data;
    }];

    $httpProvider.interceptors.push('sessionHelper');

});

// 对angularJS的内置服务$translateProvider、$translatePartialLoaderProvider进行自定义配置
angular.module('cBoard').config(function ($translateProvider, $translatePartialLoaderProvider) {
    // 加载语言文本
    $translatePartialLoaderProvider.addPart('cboard');
    // $translateProvider.useLoader('$translatePartialLoader', {
    //     urlTemplate: 'i18n/{lang}/{part}.json'
    // });

    $translateProvider.useLoader('$translatePartialLoader', {
        urlTemplate: basePath+'/olap/i18n/{lang}/{part}.json?_btv=' + resourceVersion
    });

    $translateProvider.preferredLanguage(settings.preferredLanguage);
});