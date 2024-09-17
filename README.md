A mod I made (and ChatGPT wrote) to automatically filter out Hypixel friends that get online while the mod is active, and compares with a given list of IGNs, which, when matched, sends a custom message to them.

Credit:
- ChatGPT
- Zenakin

Future Features:
- [QOL] more robust messagedPlayer handling
- [QOL] fix restoring full config backup
- [QOL] use Callable to check when done sending messages.
- [QOL] add built in time converter ?
- [QOL] add progress bar to OneConfig notifications
- [QOL+] discord integration (remote control)

Working on next:
- [QOL] Thread.Sleep(chatCD) ??
- [QOL] support for different chat formats (bridger.land uses friend >> | hypixel uses friend > | bwp.c uses has left | etc)
- [QOL] add toggles for every notification (webhook, chat, sound)
- [QOL] add queue feature to avoid crashes when another IGN on the list joins while still messaging.
- [QOL+] remote activation and message sending

Witawy awmost done:
- fixed receipt confirmation (changed from confirmation message to confirmation code cuz im lazy)
- fixed messages sometimes not fully sending