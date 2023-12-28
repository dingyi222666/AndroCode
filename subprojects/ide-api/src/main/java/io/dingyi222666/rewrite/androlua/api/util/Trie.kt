package io.dingyi222666.rewrite.androlua.api.util

import java.util.Arrays


class Trie private constructor(
    private val c: Char,
    private val terminal: Boolean,
    private val transitions: Array<Trie?>
) :
    Comparable<Trie?> {
    override fun toString(): String {
        val sb = StringBuilder()
        sb.append(c).append(if (terminal) "(terminal)\n" else "\n")
        sb.append("Next: ")
        for (transition in transitions) {
            sb.append(transition!!.c).append(" ")
        }
        sb.append("\n")
        return sb.toString()
    }

    override operator fun compareTo(other: Trie?): Int {
        return c.code - (other?.c?.code ?: 0)
    }

    /**
     * Checks if the trie contains the given character sequence or any prefixes of the sequence.
     */
    fun find(seq: CharSequence): Boolean {
        if (seq.isEmpty()) {
            return false
        }
        var idx = 0
        var cur: Trie = this
        while (idx < seq.length) {
            val c = seq[idx]
            var found = false
            for (transition in cur.transitions) {
                if (transition!!.c == c) {
                    cur = transition
                    idx++
                    found = true
                    if (idx == seq.length) {
                        return cur.terminal
                    }
                    break
                } else if (transition.c > c) {
                    return false
                }
            }
            if (!found) {
                return cur.terminal
            }
        }
        return cur.terminal
    }

    fun dump(all: Boolean, onWord: (String) -> Unit) {
        dump(StringBuilder(), all, this, onWord)
    }

    private fun dump(buffer: StringBuilder, all: Boolean, trie: Trie?, onWord: (String) -> Unit) {
        for (transition in trie!!.transitions) {
            buffer.append(transition!!.c)
            if (transition.terminal) {
                onWord.invoke(buffer.toString())
                if (all) {
                    dump(buffer, true, transition, onWord)
                }
            } else {
                dump(buffer, all, transition, onWord)
            }
            buffer.setLength(buffer.length - 1)
        }
    }

    class Builder {
        private val c: Char
        private var terminal = false
        private val transitions: MutableList<Builder> = ArrayList()

        constructor() {
            c = '\u0000'
        }

        private constructor(c: Char) {
            this.c = c
        }

        private fun addTransition(c: Char, terminal: Boolean): Builder {
            var b: Builder? = null
            for (transition in transitions) {
                if (transition.c == c) {
                    b = transition
                    break
                }
            }
            if (b == null) {
                b = Builder(c)
                transitions.add(b)
            }
            b.terminal = b.terminal or terminal
            return b
        }

        fun addWord(word: String) {
            var cur = this
            val chars = word.toCharArray()
            for (i in chars.indices) {
                val c = chars[i]
                cur = cur.addTransition(c, i == chars.size - 1)
            }
        }

        fun build(): Trie {
            val transitions = arrayOfNulls<Trie>(transitions.size)
            for (i in this.transitions.indices) {
                val transition = this.transitions[i]
                transitions[i] = transition.build()
            }
            Arrays.sort(transitions)
            return Trie(c, terminal, transitions)
        }
    }

    companion object {
        fun from(vararg words: String): Trie {
            return from(listOf(*words))
        }

        fun from(words: Iterable<String>): Trie {
            val builder = Builder()
            for (word in words) {
                builder.addWord(word)
            }
            return builder.build()
        }

        fun builder(): Builder {
            return Builder()
        }
    }
}