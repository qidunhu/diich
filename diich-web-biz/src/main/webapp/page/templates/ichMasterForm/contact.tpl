<div class="title">基础信息</div>
    <div class="hint"><i class="dot"></i>标有 <em>*</em> 的为必填项 · 信息越完善，申报成功的几率越高</div>
    <!--//End-->

    <div class="st">
        <h2>联系方式</h2>
    </div>
    <!--//End-->

    <form id="contactForm" action="" class="bd horizontal">
        <div class="main">
            <div class="group" style="display: none">
                <div class="group"><label class="label" for=""><em>*</em></label></div>
                <div class="group"><label class="label" for=""><em>*</em></label></div>
            </div>
            <!--//End 没什么用就是解决一个*的bug-->

            <div class="group">
                <label class="label"><em>*</em>手机</label>
                <div class="control">
                    <input value="{{#each contacts.contentFragmentList}}{{#equal '58' attributeId }}{{content}}{{/equal}}{{/each}}" validate="true" check="required mobile" id="sj" name="sj" data-id="58" type="text" class="ipt w310" placeholder="+86">
                    <div id="sj_err" class="errors" style="display: none"><i></i>请填写手机</div>
                </div>
            </div>
            <!--//End 手机-->

            <div class="group">
                <label class="label"><em>*</em>电子邮箱</label>
                <div class="control">
                    <input value="{{#each contacts.contentFragmentList}}{{#equal '59' attributeId }}{{content}}{{/equal}}{{/each}}" validate="true" check="required email" id="yx" name="yx" data-id="59" type="text" class="ipt w310">
                    <div id="yx_err" class="errors" style="display: none"><i></i>请填写电子邮箱</div>
                </div>
            </div>
            <!--//End 电子邮箱-->

            <div class="group">
                <label class="label" for=""><em>*</em>居住地址</label>
                <div class="control">
                    <div id="live" class="ipt w650 select" data-type="selectArea" value="" data-id="55" >请选择地域</div>
                    <div class="area" id="area" style="{{#if contact.live}}display: block;{{/if}}" >
                        <div class="select" id="select" style="{{#if contact.live}}display: none;{{/if}}"></div>
                        <div class="selected" id="selected">
                            {{#if contact.live}}
                                <li><span>{{../contact.live}}<i class="icon"></i></span></li>
                            {{/if}}
                        </div>
                    </div>
                    <div class="errors" style="display: none"><i></i>请填写地域</div>
                </div>
            </div>
            <!--//End 地域-->

            <div class="group">
                <label class="label"><em>*</em>详情地址</label>
                <div class="control">
                    <input value="{{#each contacts.contentFragmentList}}{{#equal '54' attributeId }}{{content}}{{/equal}}{{/each}}" validate="true" check="required" id="dz" name="dz" data-id="54" type="text" class="ipt w650">
                    <div id="dz_err" class="errors" style="display: none"><i></i>请填写详情地址</div>
                </div>
            </div>
            <!--//End 详情地址-->

            <div class="group">
                <label class="label"><em>*</em>邮编</label>
                <div class="control">
                    <input value="{{#each contacts.contentFragmentList}}{{#equal '56' attributeId }}{{content}}{{/equal}}{{/each}}" validate="true" check="required zipcode" id="yb" name="yb" data-id="56" type="text" class="ipt w310">
                    <div id="yb_err" class="errors" style="display: none"><i></i>请填写邮编</div>
                </div>
            </div>
            <!--//End 邮编-->

        </div>
        <!--//End-->
        <div class="handle">
            <a href="">删除此项</a>
            <a id="contact_active" class="active" href="javascript:void(0);">下一步</a>
            <a href="">跳过此项</a>
        </div>
    </form>