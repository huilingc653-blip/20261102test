'use strict';
cBoard.directive('corpSelectTree', function () {
    return {
        require: '?ngModel',
        restrict: 'A',
        link: function ($scope, element, attrs, ngModel) {
            var setting = {
                data: {
                    simpleData: {
                        enable: true
                    }
                },
                callback: {
                    onClick: function (event, treeId, treeNode) {
                        var corpId = treeNode.id;
                        jQuery.get(basePath + "/sys/user/listByCorpId", {corpId: corpId}, function (nodes) {
                            var usersShowList = [];
                            angular.forEach(nodes, function (data, key) {
                                data.corpCode = treeNode.code;
                                data.corpId = corpId;
                                data.corpName = treeNode.name;
                                usersShowList.push(data);
                            });
                            $scope.users = usersShowList;
                            $scope.$apply();

                        }, "JSON");
                    }
                }
            };
            function treeDataInit(nodes) {
                var temp = [];
                angular.forEach(nodes, function(data, key){
                    data["name"] = "("+data.code+") "+data.name;
                    if (data.children && data.children.length > 0) {
                        data.children = treeDataInit(data.children);
                    }
                    temp.push(data);
                });
                return temp;
            }
            jQuery.get(basePath + "/sys/corp/tree", function (nodes) {
                var temp = treeDataInit(nodes);
                // 初始化ztree
                var ztree = jQuery.fn.zTree.init(element, setting, temp);
                ztree.expandAll(true);
            }, "JSON");
        }
    };
});  