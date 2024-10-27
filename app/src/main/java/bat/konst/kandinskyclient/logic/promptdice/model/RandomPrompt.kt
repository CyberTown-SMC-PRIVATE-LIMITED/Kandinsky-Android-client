package bat.konst.kandinskyclient.logic.promptdice.model

import bat.konst.kandinskyclient.app.KANDINSKY_STYLE_DEFAULT

data class RandomPrompt(
    val prompt: String,
    val style: String = KANDINSKY_STYLE_DEFAULT,
    val negativePrompt: String = ""
)
