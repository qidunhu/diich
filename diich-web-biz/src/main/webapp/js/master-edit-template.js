var edit_project_tmp =
    '<div class="edit-project-tool-bar">' +
    '<div class="inner">' +
    '<button class="ui save button" style="display: none;">暂存</button>' +
    '<button class="ui add button">新增项</button>' +
    '<button class="ui submit primary button">提交</button>' +
    '<button class="ui abandon red button">关闭</button>' +
    '</div>' +
    '<div>';

var edit_main_info_tmp =
    '<form class="bd horizontal">'+
    '    <div class="group"> '+
    '        <label class="label"><em>*</em>中文名</label> '+
    '        <div class="control">'+
    '            <input type="text" class="ipt w562 data-item" data-id="13"> '+
    '            <div class="errors" style="display: none"><i></i>请填入正确格式的中文名且长度在1-50之间</div>'+
    '        </div> '+
    '    </div> '+
    '    <div class="group"> '+
    '        <label class="label"><em>*</em>非遗编码</label> '+
    '        <div class="control"> '+
    '            <input type="text" class="ipt w562 data-item" data-id="12">'+
    '            <div class="errors" style="display: none"><i></i>请填入正确格式的非遗编码且长度在1-50之间</div> '+
    '        </div> '+
    '    </div> '+
    '    <div class="group"> '+
    '        <label class="label"><em>*</em>ID编码</label> '+
    '        <div class="control"> '+
    '            <input type="text" class="ipt w562 data-item" data-id="11">'+
    '            <div class="errors" style="display: none"><i></i>请填入正确格式的ID编码且长度在1-50之间</div> '+
    '        </div> '+
    '    </div> '+
    '    <div class="group"> '+
    '        <label class="label" for=""><em>*</em>题图</label> '+
    '        <div class="control"> '+
    '            <div class="file_up topic-image">'+
    '                <span class="text">上传题图</span>'+
    '            </div> '+
    '            <div class="tips">建议比例：x*x，不符合的图片将进行裁剪；格式：jpg,png</div> '+
    '            <div class="errors editListen" style="display: none"><i></i>请上传题图</div> '+
    '        </div> '+
    '    </div> '+
    '    <div class="group"> '+
    '        <label class="label" for=""><em>*</em>申报地区</label> '+
    '        <div class="control">'+
    '            <div class="ipt w650 select editListen data-item" data-id="23" dic-type="101" id="area_temp" value="">请选择申报地区</div> '+
    '            <div class="errors" style="display: none"><i></i>请填写申报地区</div> '+
    '        </div> '+
    '        <div class="dropbox">'+
    '            <div class="item"> '+
    '                <dl class="level"> '+
    '                    <dd> '+
    '                        <ul></ul> '+
    '                    </dd> '+
    '                </dl> '+
    '            </div> '+
    '        </div> '+
    '    </div> '+
    '    <div class="group"> '+
    '        <label class="label" for=""><em>*</em>非遗等级</label>'+
    '        <div class="control"> <select class="ipt w310 editListen data-item" data-id="111" name="" id="certselect"></select> <div class="errors" style="display: none"><i></i>请选择非遗等级</div> </div>'+
    '    </div> '+
    '</form>';

var show_main_info_template =
    '<div class="read-piece">'+
    '    <div class="detail_title">'+
    '        <h2 data-id="13" class="data-item title"></h2>'+
    '        <div class="doi_code">'+
    '            <i class="icon">ID</i>'+
    '            <span>'+
    '                <em id="doi_code" data-id="11" class="data-item"></em>'+
    '            </span>'+
    '        </div>'+
    '    </div>'+
    '    <div class="bd subtxt">'+
    '        <span>'+
    '            <strong>地区：</strong>'+
    '            <em class="value dic data-item" dic-type="101" lang="chi" data-id="23"></em>'+
    '        </span>'+
    '    </div>'+
    '    <div class="bd batch" id="mas">'+
    '        <div class="tname">'+
    '            非遗在中国'+
    '            <i></i>'+
    '        </div>'+
    '        <div class="subcon" id="subcon">'+
    '            <span>级别： <em style="font-size: 12px" class="value dic data-item" dic-type="106" lang="chi" data-id="111"></em> </span>'+
    '            <span>非遗编码：<em style="font-size: 12px" class="data-item" data-id="12"></em></span>'+
    '        </div>'+
    '    </div>'+
    '</div>';

var edit_image_text_tmp =
    '<div class="image-text"> '+
    '    <div> '+
    '        <div class="text"> '+
    '            <script class="editor data-item" type="text/plain"></script> '+
    '            <div class="errors" style="display: none">'+
    '                <i></i>'+
    '                <span></span>'+
    '            </div> '+
    '        </div> '+
    '        <div class="images" id="images"> '+
    '            <div class="image-container"> </div> '+
    '            <div class="handle"> '+
    '                <div class="add file_up" data-type="image"> '+
    '                    <span class="icon"> <i></i></span> '+
    '                    <span>添加图片</span> '+
    '                </div> '+
    '                <div class="add file_up" data-type="video" style="margin-right:0;"> '+
    '                    <span class="icon icon2"><i></i></span> '+
    '                    <span>添加视频</span> '+
    '                </div> '+
    '            </div> '+
    '        </div> '+
    '    </div> '+
    '</div>';

var show_image_text_template =
    '<article class="text_img read-piece">'+
    '    <div class="side" style="margin-right: 30px; width: 770px;">'+
    '        <div class="item data-item item-content" data-id="">'+
    '            '+
    '        </div>'+
    '    </div>'+
    '    <div class="media">'+
    '        <ul>'+
    '        </ul>'+
    '    </div>'+
    '</article>';

var custom_show_tmp =
    '<article class="text_img read-piece">'+
    '    <div class="side">'+
    '        <div class="item item-content"></div>'+
    '    </div> '+
    '    <div class="media"> '+
    '        <ul> '+
    '        </ul>'+
    '        <div class="more"></div> '+
    '    </div> '+
    '</article>';

var custom_image_text_tmp =
    '<section  name="custom" id="custom" class="bd floor odd" data-type="image-text">'+
    '    <div class="card">'+
    '        <header class="title handle-button"><h4></h4>'+
    '            <select name="titles" class="ui search dropdown"></select>'+
    '            <span class="edit link" style="display: none;">编辑</span><span class="save link">保存</span><span class="cancel link">取消</span>'+
    '        </header>'+
    '    </div>'+
    '</section>';

var edit_short_text_tmp =
    '<div class="group">' +
    '   <label class="label"><em>*</em></label> ' +
    '   <div class="control"> ' +
    '       <input type="text" class="ipt w562 data-item"> ' +
    '       <div class="errors" style="display: none"><i></i>请填入正确格式的拼音且长度在1-50之间</div> ' +
    '   </div> ' +
    '</div>';

var show_short_text_template =
    '<li>'+
    '    <span class="key"></span>'+
    '    <span class="value"></span>'+
    '</li>';

var upload_form_template =
    '<form class="upload" method = "POST" action="" method="post" enctype="multipart/form-data">'+
    '<input class="_token" type="hidden" name="OSSAccessKeyId" value="">'+
    '<input class="_token" type="hidden" name="policy" value="">'+
    '<input class="_token" type="hidden" name="Signature" value="">'+
    '<input class="_token" type="hidden" name="key" value="">'+
    '<input class="_token" type="hidden" name="Filename" value="">'+
    '<input class="_token" type="hidden" name="success_action_status" value="200">'+
    '<div class="progress">' +
    '<div class="ui loader" style="width: 40px;height: 40px;position: absolute;top: 50%;left: 50%;display: block;"></div>'+
    '</div>' +
    '<input class="file" type="file" name="file">'+
    '</form>';

var living_city =
    '<div class="group"> '+
    '    <label class="label" for=""><em>*</em>居住城市</label> '+
    '        <div class="control">'+
    '            <div class="ipt w650 select editListen data-item dic" data-id="55" dic-type="101" id="living_city" value="">请选择居住城市</div> '+
    '            <div class="errors" style="display: none"><i></i>请填写居住城市</div> '+
    '        </div> '+
    '        <div class="dropbox">'+
    '            <div class="item"> '+
    '                <dl class="level"> '+
    '                    <dd> '+
    '                        <ul></ul> '+
    '                    </dd> '+
    '                </dl> '+
    '            </div> '+
    '        </div> '+
    '    </div> ';
