let home = {
    apiHost: 'KAKAO',
    // apiHost: 'NAVER',
    size: 10,
    maxSize: 50,
    currentPage: 1,
    maxPage: 50,
    pageSize: 5,
    totalCount: 0,
    drawPagination: function(pageableCount) {
        home.totalCount = pageableCount;
        let totalPage = Math.ceil(home.totalCount / home.size);
        let pageGroup = Math.ceil(home.currentPage / home.pageSize);
        let lastNumber = pageGroup * home.pageSize;
        if(lastNumber > totalPage) {
            lastNumber = totalPage;
        }
        let firstNumber = lastNumber - (home.pageSize - 1);
        if(firstNumber <= 0) {
            firstNumber = 1;
        }
        const next = lastNumber + 1;
        const prev = firstNumber - 1;

        let $liPrev = $('<li>', {class: 'page-item'});
        let $aPrev = $('<a>', {class: 'page-link', href: 'javascript:void(0)', text: '이전'});
        if(pageGroup === 1) {
            $liPrev.addClass('disabled');
            $aPrev.attr('tab-index', -1);
        } else {
            $aPrev.on('click', {prevData: prev}, function(d){ home.search(d.data.prevData) });
        }
        $liPrev.append($aPrev);
        $('#pagination-ul').append($liPrev);
        for(let i=firstNumber; i<=lastNumber; i++) {
            let $li = $('<li>', {class: 'page-item'});
            let $a = $('<a>', {class: 'page-link', href: 'javascript:void(0)', text: i});
            $a.on('click', {idxData: i}, function(d){
                home.search(d.data.idxData)
            });
            $li.append($a);
            if(i === home.currentPage) {
                $li.addClass('active');
            }
            $('#pagination-ul').append($li);
        }
        let $liNext = $('<li>', {class: 'page-item'});
        let $aNext = $('<a>', {class: 'page-link', href: 'javascript:void(0)', text: '다음'});
        if(pageGroup > totalPage - home.pageSize || lastNumber >= 50) {
            $liNext.addClass('disabled');
            $aNext.attr('tab-index', -1);
        } else {
            $aNext.on('click', {nextData: next}, function(d){ home.search(d.data.nextData) });
        }
        $liNext.append($aNext);
        $('#pagination-ul').append($liNext);
    },
    search: function(pageNumber) {
        let searchText = $('#search-text-input').val();

        if (!searchText) {
            $('#search-text-input').focus();
            return;
        }

        home.currentPage = pageNumber || 1;
        return $.ajax({
            type: 'get',
            url: '/v1/blog/search',
            dataType: 'json',
            data: {
                apiHost: home.apiHost,
                query: searchText,
                sort: $('#sort-select').val(),
                page: home.currentPage,
                size: home.size
            },
            beforeSend: function () {
                $('#result-table tbody').empty();
                $('#pagination-ul').empty();
                $('#search-button').prop('disabled', true);
            },
            success: function (response) {
                home.apiHost = response.apiHost;

                if (!response || response.pageableCount === 0) {
                    let $tr = $('<tr>');
                    let $td = $('<td>', {text: '검색 결과가 없습니다.', colSpan: 6});
                    $td.css('text-align', 'center');
                    $tr.append($td);
                    $('#result-table tbody').append($tr);

                    return;
                }

                home.drawPagination(response.pageableCount);

                let $fragment = $(document.createDocumentFragment());
                $.each(response.documents, function (idx, doc) {
                    let $tr = $('<tr>');
                    let $titleTd = $('<td>').html(doc.title);
                    let $contentsTd = $('<td>').html(doc.contents);
                    let $urlTd = $('<td>');
                    let $a = $('<a>', {href: doc.url, text: '바로가기'});
                    $urlTd.append($a);
                    let $blogNameTd = $('<td>', {text: doc.blogName});
                    let $thumbnailTd = $('<td>');
                    let $img = $('<img>', {src: doc.thumbnail});
                    $thumbnailTd.append($img);
                    let $datetimeTd = $('<td>', {text: doc.datetime.replace('T', ' ')});

                    $tr.append($titleTd, $contentsTd, $urlTd, $blogNameTd, $thumbnailTd, $datetimeTd);
                    $fragment.append($tr);
                });
                $('#result-table tbody').append($fragment);
            },
            error: function (request, status, error) {
                console.log("home.search");
                console.log("code:" + request.status);
                console.log("message:" + request.responseText);
                console.log("error:" + error);
                alert('잠시 후 다시 시도해 주시기 바랍니다.\n문제가 지속되면 관리자에게 문의해 주세요.');
            },
            complete: function () {
                home.getTop10();
                $('#search-button').prop('disabled', false);
            }
        });
    },
    getTop10: function(){
        return $.ajax({
            type: 'get',
            url: '/v1/blog/top10',
            dataType: 'json',
            beforeSend: function () {
                $('#search-ranking-table tbody').empty();
            },
            success: function (response) {
                $.each(response, function (idx, v) {
                    let $tr = $('<tr>', {'data-search-text': v.searchText});
                    $tr.on('click', function () {
                        $('#search-text-input').val($(this).attr('data-search-text'));
                        home.search();
                    });
                    let $tdNum = $('<td>', {text: idx + 1});
                    let $tdText = $('<td>');
                    let $spanText = $('<span>', {text: v.searchText});
                    $tdText.append($spanText);
                    let $tdCount = $('<td>', {text: v.searchCount});

                    $tr.append($tdNum, $tdText, $tdCount);
                    $('#search-ranking-table tbody').append($tr);
                });
            },
            error: function (request, status, error) {
                console.log("home.getTop10");
                console.log("code:" + request.status);
                console.log("message:" + request.responseText);
                console.log("error:" + error);
            },
        })
    }
}