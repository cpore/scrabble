#!/bin/bash

ant compile

java -Xmx1024m -cp bin:libs/* com.d09e.scrabble.Scrabble
