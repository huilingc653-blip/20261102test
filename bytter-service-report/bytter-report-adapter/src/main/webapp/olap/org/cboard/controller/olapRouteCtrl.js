/**
 * 页面导航器Controller
 *
 * 后端返回targetPageUrl参数，前端根据targetPageUrl参数进行路由导航。
 *
 * Created by chenlw on 2019/11/25.
 */
cBoard.controller('olapRouteCtrl', function ($rootScope, $scope, $state, $http, $filter, $uibModal, $timeout, ModalUtils) {


    var targetPageUrlElement = document.getElementById("targetPageUrlElement");

    var boardIdElement = document.getElementById("boardIdElement");
    if (boardIdElement && boardIdElement.value) {
        // 跳转到看板页面
        $state.go(targetPageUrlElement.value, {
            id: boardIdElement.value
        });
        return;
    }

    if (targetPageUrlElement.value) {
        $state.go(targetPageUrlElement.value);
        return;
    }


});