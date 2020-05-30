#include <bits/stdc++.h>

using namespace std;
int counter[1000001];
int solution[1000001][3][3];

#define endl "\n"
int main()
{
    ios_base::sync_with_stdio(0);
    cin.tie(0);
    cout.tie(0);
    int n, m, number;
    cin >> n >> m;
    for (int i = 0; i < n; ++i)
    {
        cin >> number;
        ++counter[number];
    }

    for (int i = 1; i <= m; ++i)
    {
        for (int w = 0; w <= 2; ++w) //表示上一层的abC的数量
        {
            for (int j = 0; j <= 2; ++j) //表示aBc最多有几个
            {
                for (int k = 0; k <= 2; ++k) //表示abC最多有几个
                {
                    if (counter[i] - w - j - k >= 0)
                    {
                        solution[i][j][k] = max(solution[i][j][k], solution[i - 1][w][j] + k + (counter[i] - w - j - k) / 3);
                    }
                }
            }
        }
    }

    cout << solution[m][0][0] << "\n";

    return 0;
}