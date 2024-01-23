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
  formData = new FormData();
  responseData = '';
  fileLayout = '';
  errorMessage = '';
  options: string[] = ['Person', 'Person Long Names', 'Car', 'Car with Long Data'];
  optionMapper: { [key: string]: string; } = {
    'Person' : 'PERSON',
    'Car' : 'CAR' ,
    'Car with Long Data' : 'CAR2',
    'Person Long Names' : 'PERSON2'
  };


  constructor(private http: HttpClient, private remoteService: RemoteService) {}

  onFileSelected(event: any): void {
    this.responseData = '';
    this.errorMessage = '';
    this.formData = new FormData();
    const file: File = event.target.files[0];
    if (file) {
      this.fileName = file.name;
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
