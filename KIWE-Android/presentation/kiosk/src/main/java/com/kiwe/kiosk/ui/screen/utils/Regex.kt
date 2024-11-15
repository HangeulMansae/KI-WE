package com.kiwe.kiosk.ui.screen.utils

// val menuItems =
//    listOf(
//        "아메리카노",
//        "카페\\s*라떼",
//        "카푸치노",
//        "티라미수 케이크",
//        "티라미수케익",
//        "티라미수\\s*케익",
//        "티라미스\\s*케잌",
//        "둥글레차",
//        "핫초코",
//    )
//
// val menuPattern = menuItems.joinToString("|") { "(?:$it)" } // 비포획 그룹으로 각 항목 감싸기
// val menuRegex = Regex(menuPattern)

val menuItems =
    listOf(
        "아메리카노" to listOf("아메리카노", "아메리카", "아메"),
        "디카페인 아메리카노" to listOf("디카페인 아메리카노", "디카페인 아메", "디카 아메리카노"),
        "디카페인 꿀아메리카노" to listOf("디카페인 꿀아메리카노", "디카페인 꿀 아메", "디카 꿀아메리카노"),
        "디카페인 헤이즐넛아메리카노" to listOf("디카페인 헤이즐넛아메리카노", "디카페인 헤이즐 아메", "디카 헤이즐넛 아메리카노"),
        "디카페인 바닐라아메리카노" to listOf("디카페인 바닐라아메리카노", "디카페인 바닐라 아메", "디카 바닐라 아메리카노"),
        "디카페인 카페라떼" to listOf("디카페인 카페라떼", "디카페인 라떼", "디카 라떼"),
        "디카페인 바닐라라떼" to listOf("디카페인 바닐라라떼", "디카 바닐라 라떼", "디카페인 바닐라 라떼"),
        "디카페인 연유라떼" to listOf("디카페인 연유라떼", "디카 연유 라떼", "디카페인 연유 라떼"),
        "디카페인 카라멜마끼아또" to listOf("디카페인 카라멜마끼아또", "디카페인 카라멜 마끼아또", "디카 카라멜마끼아또"),
        "디카페인 카페모카" to listOf("디카페인 카페모카", "디카페인 모카", "디카 모카"),
        "디카페인 카푸치노" to listOf("디카페인 카푸치노", "디카 카푸치노", "디카페인 카푸치노"),
        "디카페인 헤이즐넛라떼" to listOf("디카페인 헤이즐넛라떼", "디카페인 헤이즐라떼", "디카 헤이즐넛 라떼"),
        "디카페인 티라미수라떼" to listOf("디카페인 티라미수라떼", "디카페인 티라미수 라떼", "디카 티라미수 라떼"),
        "디카페인 메가리카노" to listOf("디카페인 메가리카노", "디카 메가리카노", "디카페인 메리카노"),
        "수박 화채 스무디" to listOf("수박 화채 스무디", "수박 스무디", "수박화채 스무디"),
        "코코넛 커피 스무디" to listOf("코코넛 커피 스무디", "코코넛 스무디", "코코 커피 스무디"),
        "플레인퐁크러쉬" to listOf("플레인퐁크러쉬", "플레인 퐁크러쉬", "플레인퐁 크러쉬"),
        "초코허니퐁크러쉬" to listOf("초코허니퐁크러쉬", "초코 허니퐁크러쉬", "초코퐁크러쉬"),
        "슈크림허니퐁크러쉬" to listOf("슈크림허니퐁크러쉬", "슈크림 허니퐁크러쉬", "슈허퐁"),
        "딸기퐁크러쉬" to listOf("딸기퐁크러쉬", "딸기퐁", "딸퐁크러쉬"),
        "바나나퐁크러쉬" to listOf("바나나퐁크러쉬", "바퐁크러쉬", "바나나퐁"),
        "스모어블랙쿠키프라페" to listOf("스모어블랙쿠키프라페", "스모어 블랙쿠키 프라페", "스모어 블랙 프라페"),
        "쿠키프라페" to listOf("쿠키프라페", "쿠키 프라페", "쿠프"),
        "딸기쿠키프라페" to listOf("딸기쿠키프라페", "딸쿠프", "딸기 쿠프"),
        "민트프라페" to listOf("민트프라페", "민트 프라페", "민프"),
        "커피프라페" to listOf("커피프라페", "커프", "커피 프라페"),
        "리얼초코프라페" to listOf("리얼초코프라페", "리얼 초코 프라페", "초코프라페"),
        "녹차프라페" to listOf("녹차프라페", "녹프", "녹차 프라페"),
        "유니콘프라페" to listOf("유니콘프라페", "유니 프라페", "유프"),
        "스트로베리치즈홀릭" to listOf("스트로베리치즈홀릭", "스트로베리 치즈홀릭", "치즈홀릭"),
        "플레인요거트스무디" to listOf("플레인요거트스무디", "플요스무디", "플레인 요거트 스무디"),
        "딸기요거트스무디" to listOf("딸기요거트스무디", "딸요스무디", "딸기 요거트 스무디"),
        "망고요거트스무디" to listOf("망고요거트스무디", "망요스무디", "망고 요거트 스무디"),
        "수박 주스" to listOf("수박 주스", "수박주스", "수주스"),
        "레드오렌지자몽주스" to listOf("레드오렌지자몽주스", "레드자몽주스", "자몽주스"),
        "샤인머스켓그린주스" to listOf("샤인머스켓그린주스", "샤인그린주스", "샤인머스켓주스"),
        "딸기주스" to listOf("딸기주스", "딸주", "딸기 주스"),
        "딸기바나나주스" to listOf("딸기바나나주스", "딸바주스", "딸기 바나나 주스"),
        "메가에이드" to listOf("메가에이드", "메에이드", "메가 에이드"),
        "레몬에이드" to listOf("레몬에이드", "레몬 에이드", "레에이드"),
        "블루레몬에이드" to listOf("블루레몬에이드", "블레에이드", "블루 레몬에이드"),
        "자몽에이드" to listOf("자몽에이드", "자에이드", "자몽 에이드"),
        "청포도에이드" to listOf("청포도에이드", "청에이드", "청포 에이드"),
        "유니콘매직에이드(블루)" to listOf("유니콘매직에이드(블루)", "유니콘 블루에이드", "유매블루"),
        "유니콘매직에이드(핑크)" to listOf("유니콘매직에이드(핑크)", "유니콘 핑크에이드", "유매핑크"),
        "체리콕" to listOf("체리콕", "체콕", "체리 콕"),
        "라임모히또" to listOf("라임모히또", "라모히", "라임 모히또"),
        "딸기라떼" to listOf("딸기라떼", "딸라떼", "딸기 라떼"),
        "아이스초코" to listOf("아이스초코", "아초코", "아이스 초코"),
        "오레오초코라떼" to listOf("오레오초코라떼", "오초라떼", "오레오 초코라떼"),
        "메가초코" to listOf("메가초코", "메초", "메가 초코"),
        "녹차라떼" to listOf("녹차라떼", "녹라떼", "녹차 라떼"),
        "곡물라떼" to listOf("곡물라떼", "곡라떼", "곡물 라떼"),
        "고구마라떼" to listOf("고구마라떼", "고라떼", "고구마 라떼"),
        "토피넛라떼" to listOf("토피넛라떼", "토라떼", "토피넛 라떼"),
        "로얄밀크티라떼" to listOf("로얄밀크티라떼", "로밀라떼", "로얄 밀크티 라떼"),
        "흑당라떼" to listOf("흑당라떼", "흑라떼", "흑당 라떼"),
        "흑당밀크티라떼" to listOf("흑당밀크티라떼", "흑밀라떼", "흑당 밀크티 라떼"),
        "흑당버블라떼" to listOf("흑당버블라떼", "흑버라떼", "흑당 버블 라떼"),
        "흑당버블밀크티라떼" to listOf("흑당버블밀크티라떼", "흑버밀라떼", "흑당 버블 밀크티 라떼"),
        "핫초코" to listOf("핫초코", "핫 초코"),
        "콜드브루오리지널" to listOf("콜드브루오리지널", "콜오리", "콜드 브루 오리지널"),
        "콜드브루라떼" to listOf("콜드브루라떼", "콜라떼", "콜드 브루 라떼"),
        "콜드브루 디카페인" to listOf("콜드브루 디카페인", "콜디카", "콜드 브루 디카페인"),
        "아메리카노" to listOf("아메리카노", "아메리카", "아메"),
        "꿀아메리카노" to listOf("꿀아메리카노", "꿀아메", "꿀 아메리카노"),
        "헤이즐넛아메리카노" to listOf("헤이즐넛아메리카노", "헤이 아메리카노", "헤이 아메"),
        "바닐라아메리카노" to listOf("바닐라아메리카노", "바아메", "바닐라 아메리카노"),
        "큐브라떼" to listOf("큐브라떼", "큐라떼", "큐브 라떼"),
        "트로피컬 용과 티플레져" to listOf("트로피컬 용과 티플레져", "용과 티플레져"),
        "복숭아아이스티" to listOf("복숭아아이스티", "복아티", "복숭아 아이스티"),
        "사과유자차" to listOf("사과유자차", "사유차", "사과 유자차"),
        "유자차" to listOf("유자차", "유차"),
        "레몬차" to listOf("레몬차", "레차"),
        "자몽차" to listOf("자몽차", "자차"),
        "녹차" to listOf("녹차"),
        "감자빵" to listOf("감자빵"),
        "페퍼민트" to listOf("페퍼민트", "페민트"),
        "캐모마일" to listOf("캐모마일", "캐마일"),
        "얼그레이" to listOf("얼그레이", "얼그"),
    )

val menuPattern =
    menuItems
        .flatMap { (_, synonyms) ->
            synonyms.map { "(?:$it\\s*)" }
        }.joinToString("|")

val menuRegex = Regex(menuPattern)

val helpPopupRegex = Regex("도움|돔|도우미|도움이|도움말|돠|도와|돵|돠주|도와주")

val orderCommands =
    listOf(
        "주세요",
        "주문",
        "줘",
    )
val orderPattern = orderCommands.joinToString("|") { "(?:$it)" }
val orderRegex = Regex(orderPattern)

val temperatureCommands =
    listOf(
        "따뜻한",
        "따뜻",
        "뜨거운",
        "차가운",
    )
val temperaturePattern = temperatureCommands.joinToString("|") { "(?:$it)" }
val temperatureRegex = Regex(temperaturePattern)

val noCommands = listOf("아니오", "아니요", "아뇨", "안해", "넘겨", "싫어", "주문안해", "아니야")
val yesCommands = listOf("네", "내", "넵", "어", "맞어", "예", "네", "맞아", "맞아요", "그래", "좋아", "응")
val noPattern = noCommands.joinToString("|") { "(?:$it)" }
val yesPattern = yesCommands.joinToString("|") { "(?:$it)" }
val noRegex = Regex(noPattern)
val yesRegex = Regex(yesPattern)

val payCommands =
    listOf("포장", "테이크", "가져", "들고", "싸주세", "매장", "여기서", "먹고")
val payPattern = payCommands.joinToString("|") { "(?:$it)" }
val payRegex = Regex(payPattern)

val recommendRegex = Regex("추천|좋은거|뭐 없나|뭐 있나요|있나요|추천해")
