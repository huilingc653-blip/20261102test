    /**
     * 查看项目占用的保证金信息
     */
    function viewFcBondApplyInfo(itemId,itemNo){
    	layer.open({
    		type:2,
    		area:['850px','400px'],
    		maxmin:true,
    		content:basePath + "/bond/fcbondInfo/viewApplyInfo/" +itemId+"/"+itemNo
    	});
    }
