package bat.konst.kandinskyclient.logic.promptdice

import bat.konst.kandinskyclient.logic.promptdice.model.RandomPrompt
import kotlin.random.Random

fun getRandomPrompt(): RandomPrompt {
    return when(Random.nextInt(1,50)) {
        1 -> RandomPrompt("На столе тыква из стекла ,в тыкве композиция мрачный дом в  осеннем  лесу тыквы светятся. черный кот,на столе и обычные оранжевые тыквы,на заднем плане размыто осенний лес.алмазная живопись 3д рисунок эстетично красиво высокое разрешение высокая детализация", "UHD")
        2 -> RandomPrompt("на облаке над землёй вид сверху девушка в прозрачном звёздном нежном платье с длинным супер радужным ярким шлейфом четкость", "UHD")
        3 -> RandomPrompt("корабль в море. Ricardo Sanz. молодой красивый пират на корабле в шторм, борется с ветром и волнами, брызги, ветер тучи, молния акварель HDRULTRA  рендеринг")
        4 -> RandomPrompt("На столе яблоко из стекла ,в яблоке композиция коричневый дом и белый кот в  осеннем  лесу с красными листьями яблоки светятся. Белый кот,на столе и обычные красные яблоки, на заднем плане размыто осенний лес с красными листьями, красивый домик. алмазная живопись 3д рисунок эстетично красиво высокое разрешение высокая детализация", "UHD")
        5 -> RandomPrompt("кофе  на природе  кусочек торта, смородина, лайм разрезанный, цветы лаванды  близко  модернизм  наполнение  эстетика детализация профессиональное фото четкость  яркие цвета", "UHD")
        6 -> RandomPrompt("черный пейзаж  луна, городская ратуша, снег,зима  Минимализм, золота, черного,  терракотового,серебро четкая прорисовка HDRULTRA  рендеринг", "UHD")
        7 -> RandomPrompt("красота  природы, пейзаж, минимализм, эффект оранжевый, керамика, дзен, двойная экспозиция,наложение слоёв , чëрный  фон, 1050k ,5D", "UHD")
        8 -> RandomPrompt("кучевые облака.красный закат.русская деревня.полевые цветы.ромашки .мистично.сказочно.horror.")
        9 -> RandomPrompt("Атмосферно бордовое на сером осень, угол дома в стиле ар-нуво увит диким виноградом, раннее утро, крупный план, старый пергамент, цветные чернила, английская акварель прорисовка карандашом")
        10 -> RandomPrompt("Черный антропоморфный, страшно милый, немного драный, лохматый, кот  Батон  ошалевший взгляд, хвост дыбом, бежит по улочке, убегает от пятничной  белки    Шарж, гротеск, юмор, комикс, алмазная пыль, тушь, перо, смешная карикатура, высокооктановый рендер, 8k, гиперреалистично, профессиональное фото")
        11 -> RandomPrompt("В окно светит луна,уютная комната,камин,кресло,плед,на журнальном столе свечи,вино,фрукты. картина масляными красками")
        12 -> RandomPrompt("Рождество в деревне. внутренний двор с рождественской елкой. красиво украшенные дома на холмах. старинные часы на башне. снегопад. сепия. фэнтези-арт.")
        13 -> RandomPrompt("вайфу программист. красивая девушка блондинка в очках с кошачьими ушками сидит программирует за компьютером. вокруг раскиданы открытые книги. рядом чашка с кофе. на фоне сервера. фон размыт.  высокая четкость", "UHD")
        14 -> RandomPrompt("векторная линия, рисующая художественную форму лондонского сити, на белом фоне ")
        15 -> RandomPrompt("милая маленькая зебра с цветочным принтом, винтажная иллюстрация в стиле каракули")
        16 -> RandomPrompt("Рыжая лисица в кресле, Беатрис Поттер, вид сбоку без перспективы, плоская иллюстрация, белый фон")
        17 -> RandomPrompt("красивый пушистый милый котик в пижаме с принтом авокадо,  пьет капучино, сидит на подоконнике веселый, яркое солнышко, детализация высокая, дымчато-розовые тона, пепел, oil Illustrations, насыщенность, прорисовка деталей")
        18 -> RandomPrompt("черный фон черный туман маленький черный островок посреди черного озера черный силуэт лодки  черные голые деревья цветная синяя луна", "UHD")
        19 -> RandomPrompt("старая Советская комната СССР: книжки, конфеты, стол,  старый квадратный телевизор, окно с занавесками, на подоконнике  кот,  на столе ваза с цветами оранжевыми, иллюстрация, 4k")
        20 -> RandomPrompt("Запеканка из спагетти с помидорами, колбасой, сыром и томатным соусом. профессиональное ресторанное фото,разрез,нити паприки, петрушка,соус на фоне,unreal engine 5, UltraHD, (дальний план)", "UHD")
        21 -> RandomPrompt("профессиональное фото .корабль в стиле барокко.красивый  парус.зелёная река.заросли.тина.звёздное небо.млечный путь.зеркальность.", "UHD")
        22 -> RandomPrompt("плоская Картина из пластилина,нежными мазками шпателем, Африканские мотивы, темный силуэт женщины, очертания извилистых деревьев, г серых дымчатых гор, оранжевый круг, серый туманный,дымчатый чёрный, красиво, атмосферно. шедевр")
        23 -> RandomPrompt("Мир и котята Анны Ивановой, известной иллюстратора, причудливый фантастический мир, волшебные существа, котята с крыльями, парящие острова, зачарованные леса, радужное небо, игривые котята, взаимодействующие с мифическими существами, сверкающие кристаллы, неземное сияние, мечтательная и чарующая атмосфера, слияние природы и магии")
        24 -> RandomPrompt("утро, стена избы, деревянная лавочка, раскрытая книга, сухоцветы, крупный план, вдали плетень, пожухлая трава, первый снег, солнечно, атмосферный,dark botanical, высокая детализация, текстура камня")
        25 -> RandomPrompt("внутри большого прозрачного яблока осенний сказочный пейзаж с птичками, ягодами, изогнутыми красивыми корягами и мхом, свеча, вокруг яблока изогнутые красивые коряги, крупный план, 3d, профессиональное фото, реализм", "UHD")
        26 -> RandomPrompt("красивая большая резная  рождественская свеча с зимнем рисунком ,зимний пейзаж , снег , блёстки , голубовато молочный нежный фон, красиво, профессиональное фото, реалистично, 4k, высокое разрешение, мягкое освещение, эстетично, серебряные падающие снежинки на свечи")
        27 -> RandomPrompt("удивленный котенок сидит с большими глазами, рядом миска с огромной белой косточкой, на фоне забор, сухие цветы, трава высокая детализация", "UHD")
        28 -> RandomPrompt("старинное архивное фото 19-го века с потёртостями, заломами и царапинами, сепия. на фото группа грибников, перед ними лежит на земле поверженный гриб, на заднем плане огромные деревья")
        29 -> RandomPrompt("советский плакат. изображён программист за компьютером. ")
        30 -> RandomPrompt("большой блин с начинкой из взбитых сливок и клубники, кусочек белого шоколада, вишня, крем и клубничная бледно-розовая начинка, красиво выложенные под блин, посыпанные сахарной пудрой и тёртым белым шоколадом, рубиновое свечение, блеск, ультрадетализация, алмазная акварель, очень красиво, иллюстрация еды, яркие цвета, яркое освещение", "UHD")
        31 -> RandomPrompt("фотография удивленного рыбака который держит огромную рыбу. на фоне река размыто. большая детализация. фотография.", "UHD")
        32 -> RandomPrompt("учёный черный кот стоит на огромной золотой цепи. на фоне растет дуб размыто. высокая детализация. сказочно. волшебно. акварель. пергамент. прорисовка карандашем. ")
        33 -> RandomPrompt("рисунок карандашом, цвета; коричневый, черный, серый. на белом фоне. собака улыбака. юмор, смешно,мультяшно.")
        34 -> RandomPrompt("Графика, сепия, сангина, пастель, тушь, контурная прорисовка, эскизно,  бежевая тонированная бумага, line art, темный гранж, Петербург, осенний этюд, Хорст Янссен")
        35 -> RandomPrompt("Красивая роскошная женщина в шикарном зимнем русском  наряде княгини в Москве  зимний пейзаж, живопись акриловыми красками , насыщенные яркие тона", "UHD")
        36 -> RandomPrompt("двойная экспозиция в огромном сапоге  проекция 3D избушка бабы Яги с окошками и лестницей из шнурков. волшебный свет.  микромир  макросъёмка", "UHD")
        37 -> RandomPrompt("полярная станция, метель, мгла, полярник с киркой стоит перед огромным кубом льда внутри которого монстр призрак", "UHD")
        38 -> RandomPrompt("абстрактно минимализм на черном фоне черный горизонтальный островок с черной пальмой черный оазис вокруг острова красный закат полукругом красная вода")
        39 -> RandomPrompt("прекрасная девушка в шелковой рубашке стоит на кованом балкончике, солнечно, цветы в горшках, прозрачный воздух, романтично, пастельные цвета, рисунок одной тонкой линией, акварель")
        40 -> RandomPrompt("сюрреалистическое искусство, огромный  фотоаппарат canon лежит на боку, внутри коорого осенний пейзаж и закат, осень , морская гладь  уходящая в объектив фотоаппарата, корабль свой микромир , на черном фоне приятный сюрприз внутри, концепция изображения")
        41 -> RandomPrompt("На палубе пиратского корабля капитан стоит, глядя в подзорную трубу на далёкий остров. Картина Марии Родригес, известной художницы-фантазёра, с драматичным освещением, бурными морями, потрёпанными парусами, развевающимся флагом Веселого Роджера, суровыми членами команды, работающими на заднем плане, разбросанными повсюду сундуками с сокровищами, таинственной атмосферой, ощущением приключений и опасности")
        42 -> RandomPrompt("Картина Ивана Айвазовского, переосмысленная искусственным интеллектом, на которой изображены русский и английский корабли, участвующие в ожесточённом сражении в штормовом море. Впечатляющие волны, драматическое освещение, сложные детали кораблей, историческая достоверность, напряжённое действие, мастерское владение кистью, динамичная композиция, классическое морское искусство, яркие цвета, передающие суть морских сражений XIX века")
        43 -> RandomPrompt("Фантастическая космическая панорама, парад планет, золотые звезды. из иллюминатора звездолета на звезды смотрит милый мальчик, стиль мультфильм ", "ANIME")
        44 -> RandomPrompt("professional  photo,парусник в океане на закате,  очень красиво, караваджо, кипр, динамичное небо, теплое солнце,лучи,внимание к деталям воды,снято на цифровую зеркальную камеру Canon,ultraHD", "UHD")
        45 -> RandomPrompt("professional  photo, космический корабль в космосе, очень красиво, динамичное небо, теплое солнце,лучи,внимание к деталям, снято на цифровую зеркальную камеру Canon,ultraHD", "UHD")
        46 -> RandomPrompt("Космические корабли на фоне луны. Яркие звезды. Млечный путь. профессиональное фото.  реализм. ", "UHD")
        47 -> RandomPrompt("Двойная экспозиция поверх старых новостных газет шикарный природный акварельный пейзаж")
        48 -> RandomPrompt("буря в море. вихрь, дождь. молния. одинокую лодку швыряет по волнам. в лодке сидит моряк. картина маслом. стиль Айвазовский. ")
        49 -> RandomPrompt("белое на белом все белым-бело, белое из белого и белым замело добавить щепотку игрушечного фантастического поезда выезжает из замка.пудры готики мистики фрактальность сюрреализм фантастика", negativePrompt = "темные тона")
        50 -> RandomPrompt("детализированный рисунок, красивая девушка стимпанк, одета в кожаный корсет и  чулки,кожаные шорты,сапоги,медные длинные волосы,на голове шляпа котелок с шестеренками и очками, красивая динамичная поза модели, плащ ,саквояж,стоит  на мостовой на фоне стимпанк города,фонари")
        else -> RandomPrompt("")
    }
}