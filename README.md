Project provides opportinity to count rating and build table of players using telegram bot. My bot styled for table tennis, you can find it here @fitness_one_matchmaking_bot

# How it works
Whole user experience contains from pressing buttons. Player just needs to send /start command to get main menu of application.After any operation bot will send you main menu again automatically.

## Buttons:
- Registration - Allows to registrate in table, write a name, and take basic 1500 rating points.
- Table of players - Shows list of registrated players. Every player is pressable, after it sends statistic of last players'games with name of rivals, results and accrued ratings.
- Create Game - Pressing this button sends list of players, with those ratings. Pressing at any of them starts new rating game. When game is over player which started it must choose a winner.
Game will be closed, and rating will be accrued after agreement of loser in special menu, that he will get from bot.
- My statistic - Shows statistic of current player.
- Rating button - Unpressable button, just shows players rating in menu of buttons.
- Settings - Allows rename, or delete your registration from table. Rating will be lost.
- Info - Detailed information about bot working.


## Technological stack
SpringBoot, Telegram-bot SpringBoot starter, PostgreSQL, MongoDB, Spring DATA JPA, Hibernate, Maven
