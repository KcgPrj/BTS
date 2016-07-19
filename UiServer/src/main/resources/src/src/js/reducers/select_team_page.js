/** チーム選択画面のreducer */

const initialState = {
    /**
     * チームの配列
     * @type {string[]}
     */
    teams: ['team1', 'team2', 'team3']
};

const selectTeamPage = (state = initialState, action) => {
    switch (action.type) {
        default:
            return state;
    }
};

export default selectTeamPage;