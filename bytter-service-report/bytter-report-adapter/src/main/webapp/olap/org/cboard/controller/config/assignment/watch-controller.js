// var UserRoleWatchPopCtrl = [ '$scope', '$modal', function($scope, $modal) {
//    $scope.watchUser  = function(){
//        var opts = {
//            template:  'editor-app/configuration/properties/watch-popup.html?version=' + Date.now(),
//            scope: $scope,
//            backdrop: 'static'
//        };
//        $modal(opts);
//    }
// }];

var UserRoleWatchCtrl = [ '$scope', '$translate', '$http', function($scope, $translate, $http) {

    $scope.data = {};
    $scope.dataList= [];
    $scope.cols = [0,1,2,3];
    $scope.loadding = false;

    function getQueryString() {
        var qs = location.search.substr(1), // 获取url中"?"符后的字串
            args = {}, // 保存参数数据的对象
            items = qs.length ? qs.split("&") : [], // 取得每一个参数项,
            item = null,
            len = items.length;

        for(var i = 0; i < len; i++) {
            item = items[i].split("=");
            var name = decodeURIComponent(item[0]),
                value = decodeURIComponent(item[1]);
            if(name) {
                args[name] = value;
            }
        }
        return args;
    }


    if($scope.watchData.username){
        $scope.data = {
            id:$scope.watchData.id || $scope.watchData.value,
            username:$scope.watchData.username,
            loginName:$scope.watchData.loginName
        };
        $scope.loadding = true;
        $http({method: 'GET', url: basePath+"/sys/user/userRole",params:{userId:$scope.data.id}}).success(function (data, status, headers, config) {
            $scope.dataList = data;
            $scope.loadding = false;
        }).error(function (data, status, headers, config) {
            $scope.loadding = false;
            console.log('Something went wrong when fetching stencil items:' + JSON.stringify(data));
        });
    }else{
        $scope.data = {
            id:$scope.watchData.id,
            name:$scope.watchData.name
        };
        var qs = getQueryString();
        $scope.loadding = true;
        $http({method: 'GET', url: basePath+"/sys/role/roleSpecialUser",params:{roleId:$scope.data.id,corpId:qs["corpId"]}}).success(function (data, status, headers, config) {
            $scope.dataList = data;
            $scope.loadding = false;
        }).error(function (data, status, headers, config) {
            $scope.loadding = false;
            console.log('Something went wrong when fetching stencil items:' + JSON.stringify(data));
        });
    }
	
    $scope.save = function() {

        $scope.close();
    };

    // Close button handler
    $scope.close = function() {
    	$scope.$hide();
    };
}];