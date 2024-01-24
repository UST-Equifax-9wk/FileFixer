import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { RemoteService } from '../remote.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-upload,',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './upload.component.html',
  styleUrl: './upload.component.css'
})
export class UploadComponent {

  fileName = '';
  fileSize = 0;
  formData = new FormData();
  responseData = '';
  fileLayout = '';
  fileSizeError = '';
  errorMessage = '';
  options: string[] = ['Person', 'Person with Long Data', 'Car', 'Car with Long Data'];
  optionMapper: { [key: string]: string; } = {
    'Person' : 'PERSON',
    'Car' : 'CAR' ,
    'Car with Long Data' : 'CAR2',
    'Person with Long Data' : 'PERSON2'
  };


  constructor(private http: HttpClient, private remoteService: RemoteService) {}

  onFileSelected(event: any): void {
    this.responseData = '';
    this.errorMessage = '';
    this.formData = new FormData();
    const file: File = event.target.files[0];
    if (file) {
      this.fileName = file.name;
      this.fileSize = ( file.size / 1024 / 1024 );
      console.log('fileSize', this.fileSize);
      if((this.fileSize) > 2) {
        this.fileSizeError = 'File size must be less than 2 MB.'; 
      }
      this.formData.append("flatFile", file);
    } 
  }

  selectUploadFile(): void {
    this.responseData = '';
    this.errorMessage = '';
    this.remoteService.uploadFile(this.formData,  this.optionMapper[this.fileLayout])
    .subscribe({
      next: (data) => {
        this.responseData = JSON.stringify(data.body, null, 2);
      },
      error: (error: HttpErrorResponse) => {
        this.errorMessage = error.error;
      }
    })
  }

  saveData() {
    var a = document.createElement("a");
    document.body.appendChild(a);
    var json = this.responseData,
        blob = new Blob([json], {type: "octet/stream"}),
        url = window.URL.createObjectURL(blob);
    a.href = url;
    a.download = this.fileName.split('.')[0] + ".json";
    a.click();
    window.URL.revokeObjectURL(url);
  }


}
