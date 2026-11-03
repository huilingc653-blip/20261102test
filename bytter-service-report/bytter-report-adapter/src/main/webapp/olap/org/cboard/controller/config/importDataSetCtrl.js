/**
 * Created by yfyuan on 2016/10/11.
 */
cBoard.controller('importDataSetCtrl', function ($scope, $http, $state, $stateParams, dataService, $uibModal, ModalUtils, $filter, chartService, $timeout, uuid4) {

    var translate = $filter('translate');
    $scope.optFlag = 'none';
    // resolveDataSetFlag解析状态标识：true标识解析成功，false标识解析失败
    $scope.resolveDataSetFlag = false;
    $scope.curDataset = {data: {expressions: [], filters: [], schema: {dimension: [], measure: []}}};
    $scope.alerts = [];
    $scope.verify = {dsName: true};
    $scope.loading = false;


    // 保存数据集
    $scope.save = function () {
        // if (!$scope.curDataset.name) {
        //     ModalUtils.alert(translate('CONFIG.DATASET.NAME') + translate('COMMON.NOT_EMPTY'), "modal-warning", "lg");
        //     return;
        // }
        var ds = angular.copy($scope.curDataset);
        var index = ds.name.lastIndexOf('/');
        ds.categoryName = $scope.curDataset.name.substring(0, index).trim();
        ds.name = $scope.curDataset.name.slice(index + 1).trim();
        if (ds.categoryName == '') {
            ds.categoryName = translate("COMMON.DEFAULT_CATEGORY");
        }
        $http.post("dashboard/saveImportDataset", {json: angular.toJson(ds)}).success(function (serviceStatus) {
            if (serviceStatus.status == '1') {
                $scope.verify = {dsName: true};
                ModalUtils.alert(translate("COMMON.SUCCESS"), "modal-success", "sm");
                $scope.reset();
            } else {
                ModalUtils.alert(serviceStatus.msg, "modal-warning", "lg");
            }
        });

    };

    var uploadDataSetFileElement = document.getElementById("uploadDataSetFileElement");
    uploadDataSetFileElement.onchange = function () {
        $scope.uploadFileChange();
    };

    // 获取文件后缀名
    var getFileType = function (filePath) {
        var startIndex = filePath.lastIndexOf(".");
        if (startIndex !== -1) {
            return filePath.substring(startIndex + 1, filePath.length).toLowerCase();
        } else {
            return "";
        }
    };

    $scope.uploadFileChange = function () {
        var file = uploadDataSetFileElement.files[0];
        if(file){
            var fileSuffix = getFileType(file.name);
            if (fileSuffix !== 'btcrds') {
                ModalUtils.alert("文件格式不正确，请检查文件的格式（文件后缀名为btcrds）！", "modal-warning", "lg");
                uploadDataSetFileElement.value = "";
                return;
            }
            var form = new FormData();
            form.append('datasetFile', file);
            $http({
                method: 'POST',
                url: 'dashboard/resolveDataSetFile',
                data: form,
                headers: {'Content-Type': undefined},
                transformRequest: angular.identity
            }).success(function (serviceStatus) {
                if (serviceStatus.status == "1") {
                    var dataset = serviceStatus.dataset;
                    if (dataset) {
                        //ModalUtils.alert(serviceStatus.msg, "modal-info", "lg");
                        $scope.resolveDataSetFlag = true;
                        var id = dataset.id;
                        var categoryName = dataset.categoryName;
                        var name = dataset.name;
                        var data = dataset.data;
                        if (id) {
                            $scope.curDataset.id = id;
                        }
                        if (categoryName) {
                            $scope.curDataset.name = categoryName + '/' + name;
                        } else {
                            $scope.curDataset.name = name;
                        }
                        if (data) {
                            $scope.curDataset.data = data;
                        }
                    }
                } else {
                    ModalUtils.alert(serviceStatus.msg, "modal-warning", "lg");
                    uploadDataSetFileElement.value = "";
                    $scope.resolveDataSetFlag = false;
                }
            }).error(function (error) {
                console.log('upload fail');
                ModalUtils.alert("上传文件失败！", "modal-warning", "lg");
                uploadDataSetFileElement.value = "";
            });
        }else{
            ModalUtils.alert("请上传文件！", "modal-warning", "lg");
            uploadDataSetFileElement.value = "";
            return;
        }

    };

    // 重置
    $scope.reset = function () {
        uploadDataSetFileElement.value = "";
        $scope.resolveDataSetFlag = false;
        $scope.curDataset = {data: {expressions: [], filters: [], schema: {dimension: [], measure: []}}};
    };


    // 一步完成导入数据集文件
    // 上传文件
    // $scope.uploadFileChange = function () {
    //     var file = uploadDataSetFileElement.files[0];
    //     if (!file) {
    //         ModalUtils.alert("请上传文件！", "modal-warning", "lg");
    //         uploadDataSetFileElement.value = "";
    //         return;
    //     }
    //     var fileSuffix = getFileType(file.name);
    //     if (fileSuffix !== 'btdataset') {
    //         ModalUtils.alert("文件格式不正确！", "modal-warning", "lg");
    //         uploadDataSetFileElement.value = "";
    //         return;
    //     }
    //     var form = new FormData();
    //     form.append('datasetFile', file);
    //     $http({
    //         method: 'POST',
    //         url: 'dashboard/analysisDataSetFile',
    //         data: form,
    //         headers: {'Content-Type': undefined},
    //         transformRequest: angular.identity
    //     }).success(function (res) {
    //         //console.log('upload success');
    //         //console.log(res);
    //         if (res.status == "1") {
    //             ModalUtils.info(res.msg, "modal-info", "lg");
    //         } else {
    //             ModalUtils.alert(res.msg, "modal-warning", "lg");
    //             uploadDataSetFileElement.value = "";
    //         }
    //
    //     }).error(function (error) {
    //         //console.log('upload fail');
    //         //console.log(error);
    //         ModalUtils.alert("上传文件失败！", "modal-warning", "lg");
    //         uploadDataSetFileElement.value = "";
    //     });
    // }


});
