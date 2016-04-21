<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>文章详情页</title>
    <link rel="stylesheet" href="../../css/reset.css"/>
    <style>
        .main{
            width: 800px;
            min-height: 800px;
            margin: 20px auto;
            padding: 20px;
            background: #ffffff;
            border: 1px solid #eee
        }
        .row{
            padding:5px 0;
        }
        .row span{
            width: 100px;
        }
        .row input{
            width: 500px;
            border: 1px solid #00496b;
            padding: 5px;
        }
        button{
            border: 1px solid #00496b;
            padding: 5px;
            width: 100px;
        }
        .tag-list{
            margin: 20px 0;
        }
        .tag-list li{
            border: 1px solid #00496b;
            min-width: 80px;
            text-align: center;
            display: inline-block;
            cursor: pointer;
        }
        .tag-list li.active{
            color: #ffffff;
            background: #00496b;
        }
    </style>
</head>
<body style="background: #fafafa;">
<div class="main">
    <div class="row">
        <span>标题：</span>
        <input class="art-title" type="text" >
    </div>
    <div class="row">
        <span>链接：</span>
        <input class="art-url" type="text" >
        <button class="get" type="button">获取文章</button>
    </div>
    <div>
        <div id="container"></div>
        <ul class="tag-list">
            标签列表：
            <li class="active">tag1</li>
            <li>tag2</li>
            <li>tag3</li>
            <li>tag4</li>
            <li>tag5</li>
            <li>tag6</li>
        </ul>
        <button id="submit" type="button" class="">提交</button>
    </div>
</div>
    <script type="application/javascript" src="../../js/jquery.js"></script>
    <script type="text/javascript" src="../../ueditor/ueditor.config.js"></script>
    <script src="../../ueditor/ueditor.all.min.js"></script>
    <script type="application/javascript">
        var $title = $('.art-title'), $artURL = $('.art-url'), $submit = $('#submit');
        var $tags = $('.tag-list').find('li');
        var ue = UE.getEditor('container', {
            initialFrameHeight : 400,
            initialFrameWidth : 768
        });
        ue.ready(function() {
            //获取文章
            $('.get').on('click', function(){
                var artURL = $artURL.val().trim();
                if(!!artURL){
                    $.ajax({
                        url : '/distinctclinic/article/crawl',
                        type : 'POST',
                        data : {url: artURL},
                        dataType : 'json',
                        success : function(data) {
                            if(data.result.resCode == 200) {
                                $title.val(data.item.title);
                                ue.setContent(data.item.content);
                            } else {
                                alert(data.result.errMsg);
                            }
                        },
                        error : function(msg){
                            alert('网络有错误，请稍后再试！')
                        }
                    });
                } else{
                    alert('请输入文章链接！')
                }
            });
            //选择标签
            $tags.on('click', function(){
               $(this).toggleClass('active');
            });
            //提交表单
            $submit.on('click', function(){
                var content = ue.getPlainTxt(), title = $title.val().trim(), artURL = $artURL.val().trim();
                var tags = ['tag1', 'tag2'];
                var article = {
                    title: title,
                    url: artURL,
                    sequence: 1,
                    content: content,
                    tags: tags
                };
                var otherTopic = 'string';
                $.ajax({
                    url: '/distinctclinic/article/?otherTopic=' + otherTopic,
                    type: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify(article),
                    dataType: 'json',
                    success: function (data) {
                        if(data.result.resCode == 200) {
                            location.href='/distinctclinic/article/list';
                        } else {
                            alert(data.result.errMsg);
                        }
                    },
                    error: function (msg) {
                        alert('连接网络有误！');
                    }
                });
            });

        })
    </script>
</body>
</html>