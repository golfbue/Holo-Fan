package com.example.myapplication

data class Talent(
    val name: String,
    val generation: String,
    val debutDate: String,
    val oshiMark: String,
    val bio: String,
    val profileImage: String,
    val twitter: String,
    val youtubeChannelId: String
)

object TalentProvider {
    val talents = listOf(
        Talent(
            name = "Gawr Gura",
            generation = "Hololive EN - Myth",
            debutDate = "2020-09-13",
            oshiMark = "🔱",
            bio = "Shark girl from Atlantis, cheerful and playful singer with strong meme energy.",
            profileImage = "https://yt3.ggpht.com/6BCfAqi9yIpZbHLbw9BAWySvB3XZf9r8jFqudO5nSOsHoGzLhlKrm1M1uuMCRabi_pXGDzl7=s800-c-k-c0x00ffffff-no-rj",
            twitter = "@gawrgura",
            youtubeChannelId = "UCoSrY_IQQVpmIRZ9Xf-y93g"
        ),
        Talent(
            name = "Usada Pekora",
            generation = "Hololive JP - Gen 3",
            debutDate = "2019-07-17",
            oshiMark = "👯♀️",
            bio = "Mischievous rabbit girl known for chaotic humor and iconic laugh.",
            profileImage = "https://yt3.ggpht.com/B-5Iau5CJVDiUOeCvCzHiwdkUijqoi2n0tNwfgIv_yDAvMbLHS4vq1IvK2RxL8y69BxTwmPhow=s800-c-k-c0x00ffffff-no-rj",
            twitter = "@usadapekora",
            youtubeChannelId = "UC1DCedRgGHBdm81E1llLhOQ"
        ),
        Talent(
            name = "Nakiri Ayame",
            generation = "Hololive JP - Gen 2",
            debutDate = "2018-09-03",
            oshiMark = "😈",
            bio = "Cute oni girl with charming laugh, elegant voice, and playful personality.",
            profileImage = "https://yt3.ggpht.com/3CeLWGYb6cLUywTJzNt-UpITviNxeGNvtjhIqbV-AIybCqCoFw9onWtg91bjwpqvfEP9mfqIR4Q=s800-c-k-c0x00ffffff-no-rj",
            twitter = "@nakiriayame",
            youtubeChannelId = "UC7fk0CB07ly8oSl0aqKkqFg"
        ),
        Talent(
            name = "Houshou Marine",
            generation = "Hololive JP - Gen 3",
            debutDate = "2019-08-11",
            oshiMark = "🏴☠️",
            bio = "Energetic pirate captain with strong talk skills and entertaining streams.",
            profileImage = "https://yt3.ggpht.com/RnFYoR_VkEZZ4OGRJz2cPXem1iRqMNzcGVp5LIxTRqhDu4vqckc83DBrVi2uwxiCPWEmmH6vSJk=s800-c-k-c0x00ffffff-no-rj",
            twitter = "@houshoumarine",
            youtubeChannelId = "UCCzUftO8KOVkV4wQG1vkUvg"
        ),
        Talent(
            name = "Mori Calliope",
            generation = "Hololive EN - Myth",
            debutDate = "2020-09-12",
            oshiMark = "💀",
            bio = "Reaper apprentice rapper with cool style and strong music presence.",
            profileImage = "https://yt3.ggpht.com/ZZuzZBS3JHrZz49K3ApCYQo1NQLhN3ApfW0R9hAaIfCLMfx5YTL51bOgJv0zk6Ikdngmmn0G=s800-c-k-c0x00ffffff-no-rj",
            twitter = "@moricalliope",
            youtubeChannelId = "UCL_qhgtOy0dy1Agp8vkySQg"
        ),
        Talent(
            name = "Takanashi Kiara",
            generation = "Hololive EN - Myth",
            debutDate = "2020-09-12",
            oshiMark = "🐔",
            bio = "Phoenix idol with endless energy and passionate personality.",
            profileImage = "https://yt3.ggpht.com/vnzn_RiKneABPPnp1-0SO4IAZQRXqVsL5RNDQYGR9GhT-Flm47vM4UJeyGfn4U_gteKqJMBwNA=s800-c-k-c0x00ffffff-no-rj",
            twitter = "@takanashikiara",
            youtubeChannelId = "UCHsx4Hqa-1ORjQTh9TYDhww"
        )
    )
}
