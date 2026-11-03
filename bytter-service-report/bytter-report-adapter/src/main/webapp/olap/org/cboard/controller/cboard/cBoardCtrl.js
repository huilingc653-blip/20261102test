/**
 * Created by yfyuan on 2016/7/19.
 */
cBoard.controller('cBoardCtrl', function ($rootScope, $scope, $location, $http, $q, $filter, $uibModal, ModalUtils) {

    var translate = $filter('translate');
    $rootScope.alert = function (msg) {
        ModalUtils.alert(msg);
    };

    var getMenuList = function () {
        //获取操作菜单列表
        $http.get("commons/getMenuList").success(function (response) {
            $scope.menuList = response;
        });
    };

    var getCategoryList = function () {
        //获取看板分类列表
        $http.get("dashboard/getCategoryList").success(function (response) {
            $scope.categoryList = response;
        });
    };

    var getBoardList = function () {
        //获取看板列表
        $http.get("dashboard/getBoardList").success(function (response) {
            $scope.boardList = response;
        });
    };

    $scope.$on("boardChange", function () {
        getBoardList();
    });

    $scope.$on("categoryChange", function () {
        getCategoryList();
    });

    $scope.isShowMenu = function (code) {
        //是否显示菜单
        //遍历当前用户可访问的菜单列表，如果找不到该菜单code则不显示菜单。
        return !_.isUndefined(_.find($scope.menuList, function (menu) {
            return menu.menuCode == code
        }));
    };

    // getMenuList();
    // getCategoryList();
    // getBoardList();

});