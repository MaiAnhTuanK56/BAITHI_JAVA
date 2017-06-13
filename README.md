### Bài thi môn lập trình Java
### Họ Tên: Mai Anh Tuấn
### Mã số sv: 56131368 
### Lớp: 56TH2
### Giáo viên hướng dẫn: Mai Cường Thọ
# ỨNG DỤNG NGHE NHẠC
#### `1. Giới Thiệu`
Ứng dụng cho phép nghe nhạc với số bài hát là cố định, có 2 chế độ ***bình thường*** và ***ngẫu nhiên*** với các chức năng  Play, Pause, Next, Back, Forward.
#### `2. Giao diện chính`
<img src ="http://i.imgur.com/ugVkSct.png">

* Chỉ có một màn hình, gồm giao diện chơi nhạc cùng với danh sách bài.
* Khi nghe nhạc sẽ chơi trực tiếp chơi giao diện này.
* Khi chọn bài cũng trực tiếp trên giao diện này.
* Khi chạy nhạc sẽ hiển thị được *thời gian bắt đầu*, *thời gian hiện tại*, *thời gian kết thúc*, *vị trí hiện tại của điểm seekbar*, *chế độ*, *tên bài hát*, *tên ca sĩ*.
#### `2. Chế độ`
* Gồm 2 chế độ *bình thường* và *ngẫu nhiên*.
* Ứng dụng mặc định ở chế độ *bình thường*.
* Chế độ *bình thường* khi chơi nhạc, nhạc sẽ tự động next sang bài tiếp theo, khi next đến bài cuối cùng và chơi hết nhạc sẽ hiển thị thông báo *Hết nhạc không thể next...*.
* Chế độ *ngẫu nhiên* khi chơi nhạc, nhạc sẽ tự động next ngẫu nhiên không trùng lặp bài hát, khi next ngẫu nhiên không trùng lặp hết tất cả các bài hát thì sẽ bắt đầu ngẫu nhiên không trùng lặp lại từ đầu.
* Khi đang ở chế độ *ngẫu nhiên* để chuyển về chế độ bình thường thì nhấn vào nút *ngẫu nhiên*, thì bài hát sẽ được chơi lại và chuyển về chế độ *bình thường*.
#### `3. Chức năng Play và Chức năng Pause`
* Khi chưa chọn bài hát thì sẽ yêu cầu chọn bài hát.
* Khi nhạc đang được chơi, nhấn nút *Play* sẽ chuyển thành nút *Pause* và có hiệu ứng xoay tròn.
* Khi nhạc không được chơi, nhấn nút *Pause* sẽ chuyển thanh nút *Play* và dừng hiệu ứng, nhấn nút *Play* một lần nữa nhạc sẽ tiếp tục chơi tại thời gian đó.
* Ở cả 2 chế độ *ngẫu nhiên* và *bình thường* nút *Play* và *Pause* chức năng vẫn không thay đổi.
#### `4. Chức năng Next`
* Ở chế độ *bình thường*, *Next* có chức năng next sang bài hát tiếp theo, khi next đến bài cuối cùng sẽ hiện lên thông báo *Hết nhạc không thể next...*.
* Ở chế độ *ngẫu nhiên*, *Next* có chức năng next sang bài khác một cách ngẫu nhiên không trùng lặp, khi next không trùng lặp hết tất cả các bài thì sẽ lặp lại từ đầu.
#### `5. Chức năng Back`
* Ở chế độ *bình thường*, *Back* có chức năng back về bài vừa rồi, khi back đến bài đầu tiên sẽ hiện lên thông báo *Hết nhạc không thể back...*.
* Ở chế độ *ngẫu nhiên*, *Back* có chức năng tương tự chức năng *Next*.
#### `6. Chức năng Forward`
* Chức năng này thực hiện trên thanh seekbar, cho phép nhấn, kéo, thả trên thanh seekbar, khi nhấn hoắc kéo, thả nhạc sẽ được chơi ngay tại vị trí được nhấn hay thả.
* Ở cả 2 chế độ chức năng không thay đổi.
#### `Chú ý`
Để chạy chương trình trên máy ảo bằng Eclipse ADT thì phải có DDMS>=50000ms (Window->Preferences->Android->DDMS>=50000ms)

