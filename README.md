## Command Line Editor
#### version 1.0
#### Author Ethan XuYingJun

`run EDLineEditor main()`

#### supported commands, examples:
`ed test`  *//enter editor and command mode, get test.txt content, if not existed, create it*  
`.p`       *//print current line (last line)*  
`8p`       *//print line 8(if not found, print ?)*  
`-3p`      *//print line 8(current line is 11)*  
`1,5p`     *//print lines from 1 to 5*  
`,p`       *//print all lines*  
`;p`       *//print current to last line (current is line 11)*  
`/consectetur/p`  *//forward search string "consectetur", and print it (line 2)*  
`?consectetur?p`  *//backward search string "consectetur", and print it (line 2)*  
`.a`       *//enter typing mode by append type*  
`test new text`  *//append input after the last line*  
`.`        *//quit typing mode*  
`$i`       *//enter typing mode by insert type*  
`test insert text`   *//insert input before the last line*  
`.`        *//quit typing mode*  
`1,3c`     *//enter typing mode by replace type*  
`new line words`   *//replace the line with 'new line words' for 1 - 3 lines*  
`12,13d`   *//remove line 12 to 13*  
`.=`       *//print current line number (11)*  
`3z10`     *//move line 3 content after line 10 and print line 3 to 11*  
`q`        *// type two times 'q' to quit without save*  
`Q`        *//quit without any reminder words*  
`f`        *//print default name (test)*  
`12,13W test`    *//save 12 to 13 lines to test.txt file by append type*  
`12,13m1`        *//move 12 to 13 lines insert to line 1*  
`1,2t10`         *//copy 1 to 2 lines insert to line 10*  
`1,2-12j`        *//merge 1 to lines to line 3(current line is 15)*  
`1,3s/test/new/1`   *//replace all first matched "test" words with "new" from line 1 to 3*  
`?new?ki`    *//flag matched "new" line as i (line 3)*  
`'ip`        *//print flag i line (line 3)*  
`'id`        *//delete flag i line (line 3)*  
`u`          *//undo previous delete operation*  