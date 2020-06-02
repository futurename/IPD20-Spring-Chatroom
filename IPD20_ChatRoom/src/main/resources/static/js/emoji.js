window.addEventListener('DOMContentLoaded', () => {
    const button = document.querySelector('#btEmoji');
    //const picker = new EmojiButton();

    const picker = new EmojiButton({

        // 1.0, 2.0, 3,0, 4.0, 5.0, 11.0, 12.0, 12.1
        emojiVersion: '12.1',

        // root element
        rootElement: document.body,

        // auto close the emoji picker after selection
        autoHide: true,

        // auto move focus to search field or not
        autoFocusSearch: true,

        // show the emoji preview
        showPreview: true,

        // show the emoji search input
        showSearch: true,

        // show recent emoji
        showRecents: true,

        // show skin tone variants
        showVariants: true,

        // or 'twemoji'
        style: 'native',

        // 'light', 'drak', or 'auto'
        theme: 'light',

        // maximum number of recent emojis to save
        recentsCount: 50,

        // z-index property
        zIndex: 999,

        // an array of the categories to show
        categories: [
            'smileys',
            'people',
            'animals',
            'food',
            'activities',
            'travel'
        ],

        // i18n
        i18n: {
            search: 'Search',
            categories: {
                recents: 'Recently Used',
                smileys: 'Smileys & People',
                animals: 'Animals & Nature',
                food: 'Food & Drink',
                activities: 'Activities',
                travel: 'Travel & Places'
            },
            notFound: 'No emojis found'
        }

    });


    picker.on('emoji', emoji => {

        document.querySelector(".ck-content").firstElementChild.innerHTML += emoji;
        //alert(document.querySelector(".ck-content").innerHTML);
    });

    button.addEventListener('click', () => {
        picker.pickerVisible ? picker.hidePicker() : picker.showPicker(button);
    });
});

